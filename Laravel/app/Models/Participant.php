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
         'event_id'
    ];

    protected $hidden = ['password'];

public function events()
 {
     return $this->belongsToMany(Event::class);
     }
}

