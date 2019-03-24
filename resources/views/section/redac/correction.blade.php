@extends('layouts.app')
@section('content')
    <div class="content-page">
        <!-- Start content -->
        <div class="content">
            <div class="container">
                <h3>Liste des textes</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="panel-body">
                            <div class="card-box">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Titre</th>
                                        <th>Demandé par</th>
                                        <th>Etat</th>
                                        <th>Date</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    @foreach($data as $key => $value)
                                        <tr>
                                            <th scope="row">{{ $key + 1 }}</th>
                                            <td>{{ $value->title }}</td>
                                            <td>{{ $value->send_by }}</td>
                                            <td>@if($value->is_correct)Corrigé par {{ $value->correct_by }}@else Non corrigé @endif</td>
                                            <th>{{ $value->date_post }}</th>
                                            <td>@if($value->is_correct) <a href="/section/correction/view/{{ $value->id }}" class="btn btn-info" style="margin-right: 10px">Consulter</a> @else @can('correction_do')<a href="/section/correction-text/{{ $value->id }}" class="btn btn-success" style="margin-right: 10px">Corriger</a>@endcan @endif <a href="#" class="btn btn-danger">Supprimer</a></td>
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