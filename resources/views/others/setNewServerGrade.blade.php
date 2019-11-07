@extends('layouts.app')

@section('content')

    @if(isset($Players[0]))
        <ul>
            @foreach($Players as $row)
                @foreach($row as $player)
                    <li>{{ $player['name'] }}</li>
                @endforeach
            @endforeach
        </ul>
    @else
        Aucun joueur trouvé
    @endif

@endsection