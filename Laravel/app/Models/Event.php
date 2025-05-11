<?php

namespace App\Models;

use App\Enums\Etat;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Event extends Model
{
    use HasFactory;


    protected $fillable = [
        'nom',
        'description',
        'date',
        'time',
        'localisation',
        'image',
        'animateur',
        'etat',
        'company_id'
    ];

    /**
     * The attributes that should be cast.
     *
     * @var array
     */
    protected $casts = [
        'date' => 'date',
        'etat' => Etat::class,
    ];

    public function company(): BelongsTo
    {
        return $this->belongsTo(Company::class);
    }


}
