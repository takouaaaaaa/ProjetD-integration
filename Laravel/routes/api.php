<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\EventController;

Route::post('/companies/register', [CompanyController::class, 'register']);
Route::get('/companies/getAll', [CompanyController::class, 'getAllCompanies']);
Route::get('/companies/getById/{id}', [CompanyController::class, 'getCompanyById']);
Route::get('/companies/unconfirmed', [CompanyController::class, 'getUnconfirmedCompanies']);
Route::get('/companies/confirmed', [CompanyController::class, 'getConfirmedCompanies']);
Route::put('/companies/{id}/confirm', [CompanyController::class, 'confirmCompany']);
Route::put('/companies/{id}/unconfirm', [CompanyController::class, 'unconfirmCompany']);
Route::delete('/companies/{id}/delete', [CompanyController::class, 'deleteCompany']);


Route::post('/events/addEvent', [EventController::class, 'addEvent']);
Route::get('/events/getAll', [EventController::class, 'index']);
Route::get('/events/getById/{id}', [EventController::class, 'show']);
Route::get('/events/companies/{company}/events', [EventController::class, 'indexByCompany'])
     ->name('companies.events.index')
     ->where('company', '[0-9]+');
