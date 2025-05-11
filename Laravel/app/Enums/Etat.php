<?php

namespace App\Enums;

enum Etat : String
{
    case ACCEPTE = 'ACCEPTE';
    case EN_ATTENTE= 'EN_ATTENTE';
    case REJETE = 'REJETE';
}
