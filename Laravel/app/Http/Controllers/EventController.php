<?php

namespace App\Http\Controllers;

use App\Models\Event;
use Illuminate\Http\Request;

class EventController extends Controller
{
    public function addEvent(Request $request)
    {
        $event = Event::create($request->all());

        return response()->json([
            'message' => 'Event created successfully!',
            'event' => $event
        ], 201);
    }

    public function show($id)
    {
        $event = Event::find($id);

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
}
