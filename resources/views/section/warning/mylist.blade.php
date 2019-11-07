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
                                    <th>Raison</th>
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
                                        <td>
                                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#ContentModal{{ $key }}">
                                                <i class="fas fa-eye"></i> Voir
                                            </button>

                                            <div class="modal fade" id="ContentModal{{ $key }}" tabindex="-1" role="dialog" aria-labelledby="Sanction" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered" role="document">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Sanction</h5>
                                                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                                <span aria-hidden="true">&times;</span>
                                                            </button>
                                                        </div>
                                                        <div class="modal-body">
                                                            {{ $val->text }}
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
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
