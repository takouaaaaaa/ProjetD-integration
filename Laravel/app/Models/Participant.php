<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Participant extends Model
{
    use HasFactory;

    protected $fillable = [
        'firstName',
        'lastName',
        'numTel',
        'email',
        'password',
        'role',
    ];

    protected $hidden = ['password'];


    public function events()
    {
        return $this->hasMany(Event::class);
    }
}

