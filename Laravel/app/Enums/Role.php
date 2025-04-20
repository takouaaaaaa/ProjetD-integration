<?php
// app/Enums/Role.php

namespace App\Enums;

enum Role: string 
{
    case ADMIN = 'ADMIN';
    case USER = 'USER';
    case ORGANIZATION = 'ORGANIZATION';
}
