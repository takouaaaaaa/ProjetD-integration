<?php

return [


    'paths' => ['api/*', 'sanctum/csrf-cookie'],

    'allowed_methods' => ['*'],
'allowed_origins' => [env('FRONTEND_URL', 'http://localhost:8081')], // No leading space 'allowed_origins_patterns' => [],

    'allowed_headers' => ['*'],

    'exposed_headers' => [],

    'max_age' => 0,

    'supports_credentials' => true, // We'll adjust this

];
