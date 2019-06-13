@extends('layouts.app')

@section('content')

    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <table class="table-responsive table">
                        <thead>
                            <tr>
                                <td>Pseudo</td>
                                <td>Nombre de preuves non ajoutées ces deux dernières semaines</td>
                            </tr>
                        </thead>
                        <tbody>
                            @foreach($result as $key => $counter)
                                <tr>
                                    <td>{{ $key }}</td>
                                    <td>{{ $counter }}</td>
                                </tr>
                            @endforeach
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

@endsection