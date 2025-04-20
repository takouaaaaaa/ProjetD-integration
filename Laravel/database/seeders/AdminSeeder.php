<?php
// database/seeders/AdminSeeder.php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash; // Import the Hash facade
use App\Models\Admin; // Import the Admin model
use App\Enums\Role; // Import the Role enum

class AdminSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {

        if (Admin::count() == 0) {

            $this->command->info('No admin found, creating default admin user.');

            Admin::create([
                'username' => 'admin',
                'password' => 'admin123',
                'role' => Role::ADMIN
            ]);

            $this->command->info('Default admin user created.');

        } else {
             $this->command->info('Admin user(s) already exist. Skipping creation.');
        }
    }
}
