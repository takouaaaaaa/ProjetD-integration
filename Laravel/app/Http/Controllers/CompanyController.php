<?php

namespace App\Http\Controllers;

use App\Models\Company;
use App\Enums\Role;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class CompanyController extends Controller
{
    public function register(Request $request)
    {
        // Validate incoming data, including password confirmation
        $data = $request->validate([
            'name'             => 'required|string|max:255',
            'responsable'      => 'required|string|max:255',
            'numTel'           => 'required|string|max:50',
            'email'            => 'required|email|unique:companies,email',
            'category'         => 'required|string|max:100',
            'password'         => 'required|string|min:6|confirmed',
        ]);

        // Hash the password
        $data['password'] = Hash::make($data['password']);

        // Assign a default role (Organization)
        $data['role'] = Role::ORGANIZATION;

        // Create the company record
        $company = Company::create($data);

        // Return the new company without the password field
        return response()->json($company, 201);
    }
}