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

    // Add more API methods here if needed (getAll, getById, delete, etc.)
}
