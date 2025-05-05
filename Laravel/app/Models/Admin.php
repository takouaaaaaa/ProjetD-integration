<?php
// app/Models/Admin.php

namespace App\Models;

use App\Enums\Role;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class Admin extends Authenticatable
{
    /*use HasApiTokens, HasFactory, Notifiable; // Add relevant traits*/

    /**
     * The attributes that are mass assignable.
     * Corresponds somewhat to Lombok's @AllArgsConstructor / @NoArgsConstructor allowing creation
     * Protects against mass assignment vulnerabilities.
     *
     * @var array<int, string>
     */
    protected $fillable = [
        'username',
        'password',
        'role',
    ];

    /**
     * The attributes that should be hidden for serialization.
     * Prevents the password from being included in JSON responses, etc.
     *
     * @var array<int, string>
     */
    protected $hidden = [
        'password',

    ];

    /**
     * The attributes that should be cast.
     * Automatically converts the 'role' database string to/from the Role Enum object.
     * Corresponds somewhat to @Enumerated(EnumType.STRING)
     *
     * @var array<string, string>
     */
    protected $casts = [
        'password' => 'hashed',

        'role' => Role::class,
    ];
}
