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
}
