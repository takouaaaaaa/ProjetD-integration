<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;
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
 public function event(): BelongsTo 
    {
        return $this->belongsTo(Event::class, 'event_id');
       }
}

