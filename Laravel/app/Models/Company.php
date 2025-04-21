<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use App\Enums\Role;

class Company extends Model
{
    protected $fillable = [
        'name',
        'responsable',
        'numTel',
        'email',
        'category',
        'password',
        'role',
    ];

    protected $casts = [
        'role' => Role::class,
    ];

    // Hide sensitive fields from JSON
    protected $hidden = ['password'];
}