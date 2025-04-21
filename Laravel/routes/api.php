<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CompanyController;

Route::post('/companies/register', [CompanyController::class, 'register']);