<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use App\Enums\Role;
use Illuminate\Database\Eloquent\Builder;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Company extends Model
{
    use HasFactory;
    public $timestamps = false;
    protected $fillable = [
        'name',
        'responsable',
        'numTel',
        'email',
        'category',
        'password',
        'role',
        'is_confirmed',
    ];

    protected $casts = [
        'role' => Role::class,
        'is_confirmed' => 'boolean',
    ];


    protected $hidden = ['password'];

    public function scopeConfirmed(Builder $query): void
    {
        $query->where('is_confirmed', true);
    }

    public function scopeUnconfirmed(Builder $query): void
    {
        $query->where('is_confirmed', false);
    }

    public function events(): HasMany
    {
        return $this->hasMany(Event::class);
    }

}
