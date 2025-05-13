<?php

namespace App\Http\Controllers;

use Illuminate\Database\QueryException;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use App\Models\Event;
use Illuminate\Http\Request;
use App\Models\Company;
use App\Enums\Etat;

class EventController extends Controller
{
    public function addEvent(Request $request)
    {
        try {

            $validatedData = $request->validate([
                'nom' => 'required|string|max:255',
                'description' => 'required|string',
                'date' => 'required|date',
                'time' => 'required|date_format:H:i:s',
                'localisation' => 'required|string|max:255',
                'image' => 'nullable|string|max:255',
                'animateur' => 'required|string|max:255',
                'company_id' => 'required|integer|exists:companies,id'
            ]);

            $eventData = [
                'nom' => $validatedData['nom'],
                'description' => $validatedData['description'],
                'date' => $validatedData['date'],
                'time' => $validatedData['time'],
                'localisation' => $validatedData['localisation'],
                'image' => $validatedData['image'] ?? null,
                'animateur' => $validatedData['animateur'],
                'etat' => Etat::EN_ATTENTE,
                'company_id' => $validatedData['company_id'],
                'deletion_requested' => false
            ];

            $event = Event::create($eventData);
            $event->load('company');

            return response()->json([
                'message' => 'Event created successfully!',
                'event' => $event
            ], 201);
        } catch (ValidationException $e) {
            return response()->json([
                'message' => 'Validation Failed',
                'errors' => $e->errors()
            ], 422);
        } catch (QueryException $e) {
            return response()->json([
                'message' => 'Could not create event due to a database error. ' . $e->getMessage()
            ], 500);
        } catch (\Exception $e) {
            return response()->json([
                'message' => 'An unexpected error occurred. ' . $e->getMessage()
            ], 500);
        }
    }

    public function show($id)
    {
        $event = Event::with('company')->find($id);

        if (!$event) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        }

        return response()->json($event);
    }

    public function index()
    {
        $events = Event::all();
        return response()->json($events);
    }

    public function indexByCompany(Company $company)
    {

        $events = $company->events()->with('company')->get();


        return response()->json($events);
    }
    public function acceptEvent($id)
    {
        $event = Event::find($id);

        if (!$event) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        }

        $event->etat = Etat::ACCEPTE;
        $event->save();

        return response()->json(['message' => 'Événement accepté avec succès', 'event' => $event]);
    }

    public function rejectEvent($id)
    {
        $event = Event::find($id);

        if (!$event) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        }

        $event->etat = Etat::REJETE;
        $event->save();

        return response()->json(['message' => 'Événement rejeté avec succès', 'event' => $event]);
    }

    public function getAcceptedEvents()
    {
        $events = Event::where('etat', Etat::ACCEPTE)
            ->select('nom', 'date', 'description', 'localisation', 'animateur')
            ->get();

        return response()->json($events);
    }

    public function updateEvent(Request $request, $id)
    {
        try {
            $event = Event::findOrFail($id);

            $validatedData = $request->validate([
                'localisation' => 'sometimes|string|max:255',
                'date' => 'sometimes|date',
                'time' => 'sometimes|date_format:H:i:s'
            ]);
            if ($request->has('localisation')) {
                $event->localisation = $validatedData['localisation'];
            }

            if ($request->has('date')) {
                $event->date = $validatedData['date'];
            }

            if ($request->has('time')) {
                $event->time = $validatedData['time'];
            }

            $event->save();

            return response()->json([
                'message' => 'Événement mis à jour avec succès',
                'event' => $event
            ]);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        } catch (ValidationException $e) {
            return response()->json([
                'message' => 'Erreur de validation',
                'errors' => $e->errors()
            ], 422);
        } catch (\Exception $e) {
            return response()->json([
                'message' => 'Erreur serveur',
                'error' => $e->getMessage()
            ], 500);
        }
    }

    public function getParticipants($id)
    {
        try {
            $event = Event::with('participants')->find($id);

            if (! $event) {
                return response()->json([
                    'message' => 'Événement non trouvé'
                ], 404);
            }

            $participants = $event->participants
                ->map(function ($p) {
                    return [
                        'id'        => $p->id,
                        'firstName' => $p->firstName,
                        'lastName'  => $p->lastName,
                        'email'     => $p->email,
                        'numTel'    => $p->numTel,
                    ];
                });

            return response()->json([
                'participants' => $participants,
            ]);
        } catch (\Throwable $th) {
            return response()->json(['message' => 'Erreur' . $th->getMessage()], 500);
        }
    }

    // app/Http/Controllers/EventController.php

    /**
     * Company requests deletion of its own event.
     */
    public function requestDeletion(Request $request, $id)
    {
        try {
            $event = Event::find($id);
            if (! $event) {
                return response()->json(['message' => 'Événement non trouvé'], 404);
            }

            $event->deletion_requested = true;
            $event->save();

            return response()->json([
                'message' => 'Demande de suppression soumise.',
                'event'   => $event->only('id', 'nom', 'deletion_requested'),
            ]);
        } catch (\Throwable $th) {
            return response()->json(['message' => '' . $th->getMessage()]);
        }
    }

    /**
     * Admin approves the deletion request – permanently delete the event.
     */
    public function approveDeletion($id)
    {
        $event = Event::find($id);
        if (! $event) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        }
        if (! $event->deletion_requested) {
            return response()->json([
                'message' => 'Aucune demande de suppression en attente.'
            ], 400);
        }

        $event->delete();

        return response()->json([
            'message' => 'Événement supprimé avec succès.',
        ], 200);
    }

    /**
     * Admin rejects the deletion request.
     */
    public function rejectDeletion($id)
    {
        $event = Event::find($id);
        if (! $event) {
            return response()->json(['message' => 'Événement non trouvé'], 404);
        }
        if (! $event->deletion_requested) {
            return response()->json([
                'message' => 'Aucune demande de suppression en attente.'
            ], 400);
        }

        $event->deletion_requested = false;
        $event->save();

        return response()->json([
            'message' => 'Demande de suppression rejetée.',
            'event'   => $event->only('id', 'nom', 'deletion_requested'),
        ], 200);
    }
}
