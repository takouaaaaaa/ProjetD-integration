<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CompanyController;

Route::post('/companies/register', [CompanyController::class, 'register']);
Route::get('/companies', [CompanyController::class, 'getAllCompanies']);
Route::get('/companies/{id}', [CompanyController::class, 'getCompanyById']);
