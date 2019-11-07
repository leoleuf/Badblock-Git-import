@extends('layouts.app')

@section('content')

    @if(isset($Players[0]))
        <ul>
<<<<<<< HEAD
            @foreach($Players as $row)
                @foreach($row as $player)
                    <li>{{ $player['name'] }}</li>
                @endforeach
=======
            @foreach($Players as $player)
                <li>{{ $player['name'] }}</li>
>>>>>>> 847eb807a3fb1c439fb7e1c8a08431e7d087b4db
            @endforeach
        </ul>
    @else
        Aucun joueur trouvé
    @endif

@endsection