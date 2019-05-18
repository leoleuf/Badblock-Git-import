@extends('layouts.app')

@section('content')

    <ul>
        @foreach($Players as $player)

            <li>{{ $player }}</li>

        @endforeach
    </ul>

    @endsection