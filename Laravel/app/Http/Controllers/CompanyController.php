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
        try {
            $data = $request->validate([
                'name'             => 'required|string|max:255',
                'responsable'      => 'required|string|max:255',
                'numTel'           => 'required|string|unique:companies,numTel|max:50',
                'email'            => 'required|email|unique:companies,email',
                'category'         => 'required|string|max:100',
                'password'         => 'required|string|min:6',
            ]);
        } catch (\Throwable $th) {
            return response()->json(['message' => $th->getMessage()], 404);
            
        }

        // Hash the password
        $data['password'] = Hash::make($data['password']);

        // Assign a default role (Organization)
        $data['role'] = Role::ORGANIZATION;

        // Create the company record
        $company = Company::create($data);

        // Return the new company without the password field
        return response()->json($company, 201);
    }


    public function getAllCompanies()
    {
        $companies = Company::all()->map(function ($company) {
            $company->password = null;
            return $company;
        });

        return response()->json($companies);
    }

    public function getCompanyById($id)
    {
        $company = Company::find($id);

        if (!$company) {
            return response()->json(['message' => 'Entreprise non trouvée'], 404);
        }

        $company->password = null;

        return response()->json($company);
    }
}
