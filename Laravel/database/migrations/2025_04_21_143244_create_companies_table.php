<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
{
    Schema::create('companies', function (Blueprint $table) {
        $table->id();
        $table->string('name');
        $table->string('responsable');
        $table->string('numTel');
        $table->string('email')->unique();
        $table->string('category');
        $table->string('description')->nullable(); 
        $table->string('password');
        $table->string('role');
        $table->boolean('is_confirmed')->default(false);

    });
}


    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('companies');
    }
};
