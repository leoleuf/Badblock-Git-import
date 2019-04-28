@extends('layouts.app')

@section('content')
    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <h4 class="m-t-0 header-title">Liste des avertissements</h4>
                <div class="row">
                    <div class="col-12">
                        <div class="p-20">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Pseudo</th>
                                    <th>Avertis par</th>
                                    <th>Titre</th>
                                    <th></th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                @foreach ($data as $key => $val)
                                    <tr>
                                        <th scope="row">{{ $key + 1 }}</th>
                                        <td>{{ $val->pseudo }}</td>
                                        <td>{{ $val->warn_by }}</td>
                                        <td>{{ $val->title }}</td>
                                        <td>{{ \App\Http\Controllers\DateController::formatDate($val->created_at) }}</td>
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
@endsection
