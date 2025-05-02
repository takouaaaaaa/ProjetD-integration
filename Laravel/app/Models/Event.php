<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Event extends Model
{
    use HasFactory;

    // Ensure 'events' table name is inferred correctly, or set:
    // protected $table = 'events';

    protected $fillable = [
        'nom',
        'description',
        'date',
        'time',
        'localisation',
        'image', // Only this for image
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
        // 'time' => 'datetime:H:i:s',
        // Casting image_data is usually not needed unless you want specific behavior
    ];

    public function company(): BelongsTo
    {
        return $this->belongsTo(Company::class);
    }

    // You might add accessors/mutators if you need specific handling
    // for image_data when getting/setting it, e.g., base64 encoding/decoding
    // public function getImageDataAttribute($value) {
    //     return base64_encode($value); // Example: Return as base64
    // }
}
