@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nom de l'Image</th>
                                        <th>Lien</th>
                                        <th>Date</th>
                                        <th>Aperçu</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @foreach($data as $key => $value)
                                        <tr>
                                            <th scope="row">{{ $key + 1 }}</th>
                                            <td>{{ $value->name }}</td>
                                            <td><a href="{{ $value->link }}">{{ $value->link }}</a></td>
                                            <td>{{ App\Http\Controllers\DateController::formatDate($value->date_post) }}</td>
                                            <th><img src="{{ $value->link }}" class="img-fluid" width="64"></th>
                                        </tr>
                                    @endforeach
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection