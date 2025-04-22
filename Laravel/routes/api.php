<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CompanyController;

Route::post('/companies/register', [CompanyController::class, 'register']);
Route::get('/companies/getAll', [CompanyController::class, 'getAllCompanies']);
Route::get('/companies/getById/{id}', [CompanyController::class, 'getCompanyById']);
