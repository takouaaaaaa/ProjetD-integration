<?php

namespace App\Http\Controllers;

use App\Models\Company;
use App\Enums\Role;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;

class CompanyController extends Controller
{
    public function register(Request $request)
    {
        try {
            $data = $request->validate([
                'name'             => 'required|string|max:255',
                'responsable'      => 'required|string|max:255',
                'numTel'           => 'required|string|unique:companies,numTel|max:50',
                'email'            => 'required|email|unique:companies,email',
                'category'         => 'required|string|max:100',
                'password'         => 'required|string|min:6',
            ]);

            $data['password'] = Hash::make($data['password']);
            $data['role'] = Role::ORGANIZATION;
            $data['is_confirmed'] = false;

            $company = Company::create($data);

            return response()->json($company, 201);

        } catch (ValidationException $e) {
            return response()->json(['message' => 'Validation failed', 'errors' => $e->errors()], 422);
        } catch (\Throwable $th) {
            return response()->json(['message' => 'Registration failed. '. $th->getMessage()], 500);
        }
    }

    public function getAllCompanies()
    {
        $companies = Company::all();
        return response()->json($companies);
    }

    public function getCompanyById($id)
    {
        $company = Company::find($id);

        if (!$company) {
            return response()->json(['message' => 'Entreprise non trouvée'], 404);
        }

        return response()->json($company);
    }

    public function getUnconfirmedCompanies()
    {
        $companies = Company::unconfirmed()->get();
        return response()->json($companies);
    }

    public function getConfirmedCompanies()
    {
        $companies = Company::confirmed()->get();
        return response()->json($companies);
    }

    public function confirmCompany($id)
    {
        try {
            $company = Company::findOrFail($id);
            $company->is_confirmed = true;
            $company->save();
            return response()->json($company);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Entreprise non trouvée pour confirmation'], 404);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Confirmation failed.'], 500);
        }
    }

    public function unconfirmCompany($id)
    {
        try {
            $company = Company::findOrFail($id);
            $company->is_confirmed = false;
            $company->save();
            return response()->json($company);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Entreprise non trouvée pour confirmation'], 404);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Confirmation failed.'], 500);
        }
    }
    public function deleteCompany($id)
    {
        try {
            $company = Company::findOrFail($id);
            $deleted = $company->delete();

            if ($deleted) {
                return response()->json(null, 204);
            } else {
                return response()->json(['message' => 'Suppression échouée après avoir trouvé l\'entreprise.'], 500);
            }
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Entreprise non trouvée pour suppression'], 404);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Deletion failed.'], 500);
        }
    }
}
