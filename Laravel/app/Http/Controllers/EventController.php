<?php

namespace App\Http\Controllers;

use Illuminate\Database\QueryException;
use Illuminate\Validation\ValidationException;
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
                'company.id' => 'required|integer|exists:companies,id'
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
                'company_id' => $validatedData['company']['id']
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

}
