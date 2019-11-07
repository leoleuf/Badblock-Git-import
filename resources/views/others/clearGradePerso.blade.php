@extends('layouts.app')

@section('content')


    @if(isset($Players[0]))
        <ul>
            @foreach($Players as $player)
                <li>{{ $player['name'] }}</li>
            @endforeach
        </ul>
    @else
        Aucun joueur trouvé avec un grade Legend à vie
    @endif

@endsection