@extends('layouts.app')
@section('content')
    <div class="row">
        @can('build_project_create')
            <div class="col-sm-4">
                <a href="/build/project/new" class="btn btn-success btn-rounded w-md waves-effect waves-light m-b-20">Nouveau
                    Projet
                </a>
            </div>
        @endcan
        <div class="col-sm-8">
            <div class="project-sort pull-right">
                <div class="project-sort-item">
                    <form class="form-inline">
                        <div class="form-group">
                            <label>Phase :</label>
                            <select class="form-control ml-2 form-control-sm">
                                <option>All Projects(6)</option>
                                <option>Complated</option>
                                <option>Progress</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Sort :</label>
                            <select class="form-control ml-2 form-control-sm">
                                <option>Date</option>
                                <option>Name</option>
                                <option>End date</option>
                                <option>Start Date</option>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
        </div><!-- end col-->
    </div>
    <!-- end row -->


    <div class="row">
        @foreach($data as $project)
            <div class="col-xl-4">
                <div class="card-box project-box">
                    @if($project->isFinished)
                        <div class="badge badge-success">Terminé</div>
                    @else
                        <div class="badge badge-warning">En cours</div>
                    @endif
                    @can('build_project_create')
                        <a href="/build/project/delete/{{ $project->id }}" class="btn btn-icon waves-effect waves-light btn-danger m-b-5" style="margin-bottom: 10px;"><i class="fas fa-times"></i></a>
                        @if(!$project->isFinished)
                            <a href="/build/project/check/{{ $project->id }}" class="btn btn-icon waves-effect waves-light btn-success m-b-5" style="margin-bottom: 10px;"><i class="fas fa-check"></i></a>
                        @endif
                    @endcan
                    <h4 class="mt-0"><a href="#" class="text-inverse">{{ $project->name }}</a></h4>


                    @if($project->category == "Build")
                        <p class="text-success text-uppercase m-b-20 font-13">{{ $project->category }}</p>
                    @elseif($project->category == "Terraforming")
                        <p class="text-purple text-uppercase m-b-20 font-13">{{ $project->category }}</p>
                    @else
                        <p class="text-warning text-uppercase m-b-20 font-13">{{ $project->category }}</p>
                    @endif

                    <p class="text-muted font-13">{!! $project->desc_project !!}<a href="#" class="font-600 text-muted">En
                            savoir plus</a>
                    </p>

                    <ul class="list-inline">
                        <li class="list-inline-item">
                            <h4 class="mb-0">{{ \App\Http\Controllers\DateController::formatDateWithoutTime($project->dateStart) }}</h4>
                            <p class="text-muted">Début</p>
                        </li>
                        <li class="list-inline-item">
                            <h4 class="mb-0">{{ \App\Http\Controllers\DateController::formatDateWithoutTime($project->dateEnd) }}</h4>
                            <p class="text-muted">Fin</p>
                        </li>
                        <li class="list-inline-item">
                            <h4 class="mb-0">{{ $project->price }}</h4>
                            <p class="text-muted">Gain
                                <small>(PB)</small>
                            </p>
                        </li>
                    </ul>

                    <div class="project-members m-b-20">
                        <span class="m-r-5 font-600">Team : </span>
                        @foreach(\App\Http\Controllers\build\ProjectController::convertTeam($project->team) as $info)
                            <a href="#" title="{{ $info }}" data-toggle="tooltip" data-placement="top" data-original-title="{{ $info }}">
                                <img src="https://cdn.badblock.fr/head/{{ $info }}/48.png" alt="user" class="rounded-circle thumb-sm">
                            </a>
                        @endforeach
                    </div>

                    <p class="font-600 m-b-5">Avancement <span class="text-success pull-right">0%</span></p>
                    <div class="progress progress-bar-success-alt progress-sm m-b-5">
                        <div class="progress-bar progress-bar-success progress-animated wow animated animated"
                             role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"
                             style="width: 0%; visibility: visible; animation-name: animationProgress;">
                        </div><!-- /.progress-bar .progress-bar-danger -->
                    </div><!-- /.progress .no-rounded -->

                </div>
            </div><!-- end col-->
        @endforeach
    </div>
@endsection