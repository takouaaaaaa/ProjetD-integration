<?php

namespace App\Http\Controllers;

use Illuminate\Database\QueryException;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use App\Models\Event;
use Illuminate\Http\Request;
use App\Models\Company;
use App\Enums\Etat;
use Illuminate\Support\Facades\Log;

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
                 'image' => 'nullable|image|mimes:jpeg,png,jpg,gif,svg|max:2048',
                'animateur' => 'required|string|max:255',
                'company_id' => 'required|integer|exists:companies,id',
            ]);
    $imagePath = null;
            if ($request->hasFile('image') && $request->file('image')->isValid()) {
                $imagePath = $request->file('image')->store('event_images', 'public');
            }
            $eventData = [
                'nom' => $validatedData['nom'],
                'description' => $validatedData['description'],
                'date' => $validatedData['date'],
                'time' => $validatedData['time'],
                'localisation' => $validatedData['localisation'],
                'image' => $imagePath,
                'animateur' => $validatedData['animateur'],
                'etat' => Etat::EN_ATTENTE,
                'company_id' => $validatedData['company_id']
            ];

            $event = Event::create($eventData);
            $event->load('company');

            return response()->json([
                'message' => 'Event created successfully!',
                'event' => $event
            ], 201);
        } catch (ValidationException $e) {
             Log::error('Validation failed for addEvent:', ['errors' => $e->errors(), 'request' => $request->except('image')]); 
            return response()->json([
                'message' => 'Validation Failed',
                'errors' => $e->errors()
            ], 422);
        } catch (QueryException $e) {
            return response()->json([
                'message' => 'Could not create event due to a database error.'
            ], 500);
        } catch (\Exception $e) {
            return response()->json([
                'message' => 'An unexpected error occurred.'
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
        ->select('nom', 'date','description', 'localisation', 'animateur')
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
}}
