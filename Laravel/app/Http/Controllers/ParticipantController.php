<?php

namespace App\Http\Controllers;

use App\Models\Participant;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;

class ParticipantController extends Controller
{
    public function register(Request $request)
    {
        try {
            $data = $request->validate([
                'firstName' => 'required|string|max:255',
                'lastName' => 'required|string|max:255',
                'numTel' => 'required|string|max:15|unique:participants,numTel',
                'email' => 'required|email|unique:participants,email',
                'password' => 'required|string|min:6|confirmed',
            ]);

            $data['password'] = Hash::make($data['password']);
            $data['role'] = 'PARTICIPANT'; // Default role

            $participant = Participant::create($data);

            return response()->json([
                'message' => 'Participant registered successfully.',
                'participant' => $participant
            ], 201);
        } catch (ValidationException $e) {
            return response()->json([
                'message' => 'Validation failed',
                'errors' => $e->errors()
            ], 422);
        } catch (\Throwable $th) {
            return response()->json([
                'message' => 'Registration failed. ' . $th->getMessage()
            ], 500);
        }
    }
    public function registerToEvent(Request $request, $participantId)
    {
        try {
            $request->validate([
                'event_id' => 'required|exists:events,id',
            ]);

            $participant = Participant::findOrFail($participantId);
            $eventId = $request->input('event_id');

            if ($participant->event_id === $eventId) {
                return response()->json(['message' => 'Déjà inscrit à cet événement.'], 409);
            }

            $participant->event_id = $eventId;
            $participant->save();

            return response()->json([
                'message' => 'Inscription réussie.',
            ]);
        } catch (\Throwable $th) {
            return response()->json([
                'message' => 'Registration failed. ' . $th->getMessage()
            ], 500);
        }
    }

    public function unregisterFromEvent(Request $request, $participantId, $eventId)
    {
        $participant = Participant::findOrFail($participantId);
        if ($participant->event_id === null) {
            return response()->json(['message' => 'Le participant n\'est inscrit à aucun événement.'], 400); // Bad Request
        }
        if ($participant->event_id != $eventId) {
            return response()->json(['message' => 'Le participant n\'est pas inscrit à cet événement spécifique ou l\'ID de l\'événement fourni est incorrect.'], 400); // Bad Request
        }
        $participant->event_id = null;
        $participant->save();

        return response()->json([
            'message' => 'Désinscription de l\'événement réussie.',
            'participant' => $participant
        ], 200);
    }

    public function index()
    {
        $participants = Participant::all();
        return response()->json($participants);
    }
}
