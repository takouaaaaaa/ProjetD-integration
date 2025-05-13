<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\EventController;
use App\Http\Controllers\ParticipantController;

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
Route::put('/events/{id}/accept', [EventController::class, 'acceptEvent']);
Route::put('/events/{id}/reject', [EventController::class, 'rejectEvent']);
Route::put('/events/{id}/update', [EventController::class, 'updateEvent']);

Route::post('/participants/register', [ParticipantController::class, 'register']);
Route::get('/events/accepted', [EventController::class, 'getAcceptedEvents']);
Route::post('/participants/{id}/registerEvent', [ParticipantController::class, 'registerToEvent']);
Route::delete('/participant/{id}/DeleteEvent/{eventId}', [ParticipantController::class, 'unregisterFromEvent']);
<<<<<<< HEAD
Route::get('/participants', [ParticipantController::class, 'index']);
Route::get('events/{id}/getParticipants', [EventController::class, 'getParticipants']);

Route::post('events/{id}/requestCancel', [EventController::class, 'requestDeletion']);

Route::post('admin/events/{id}/acceptCancel', [EventController::class, 'approveDeletion']);
Route::post('admin/events/{id}/rejectCancel',  [EventController::class, 'rejectDeletion']);
=======
>>>>>>> parent of c776799 (Annulation & Consultation Participants par events)
