<?php
// app/Models/Admin.php

namespace App\Models;

use App\Enums\Role; // Import the Enum
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable; // Often useful for auth later
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens; // If using Sanctum for APIs

// You might want to extend Authenticatable if you plan to use Laravel's built-in auth
// class Admin extends Model
class Admin extends Authenticatable // Using Authenticatable is common
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
        // 'email_verified_at' => 'datetime', // Common if using Authenticatable
        'password' => 'hashed', // Automatically hashes passwords when set (Laravel 10+)

        'role' => Role::class,
    ];
}
