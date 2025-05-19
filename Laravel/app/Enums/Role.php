<?php

namespace App\Enums;

enum Role: string
{
    case ADMIN = 'ADMIN';
    case PARTICPANT = 'PARTICIPANT';
    case ORGANIZATION = 'ORGANIZATION';
}
