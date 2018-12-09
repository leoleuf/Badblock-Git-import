@extends('layouts.app')

@section('header')

@endsection

@section('content')
    <div class="col-xl-3 col-md-6">
        <div class="card-box">
            <h4 class="header-title mt-0 m-b-30">Cluster : XXXXXXXXX</h4>

            <p class="font-600 m-b-5">Cpu <span class="text-primary pull-right">80%</span></p>
            <div class="progress progress-bar-danger-alt progress-sm m-b-20">
                <div class="progress-bar progress-bar-danger progress-animated wow animated animated" role="progressbar"
                     aria-valuenow="80" aria-valuemin="0" aria-valuemax="100"
                     style="width: 80%; visibility: visible; animation-name: animationProgress;">
                </div>
            </div>
            <p class="font-600 m-b-5">Ram <span class="text-primary pull-right">80%</span></p>
            <div class="progress progress-bar-primary-alt progress-sm m-b-20">
                <div class="progress-bar progress-bar-primary progress-animated wow animated animated"
                     role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100"
                     style="width: 80%; visibility: visible; animation-name: animationProgress;">
                </div>
            </div>
            <table class="tablesaw m-t-20 table m-b-0 tablesaw-stack table-scoll" data-tablesaw-mode="stack"
                   id="table-4991">
                <thead>
                <tr>
                    <th scope="col" data-tablesaw-sortable-col="" data-tablesaw-priority="persist">Server</th>
                    <th scope="col" data-tablesaw-sortable-col="" data-tablesaw-sortable-default-col=""
                        data-tablesaw-priority="3">#
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><b class="tablesaw-cell-label">hub_001</b></td>
                    <td><b class="tablesaw-cell-label">
                            <a href="/" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i
                                        class="fa fa-wrench"></i> </a>
                        </b>
                    </td>
                </tr>
                <tr>
                    <td><b class="tablesaw-cell-label">login_001</b></td>
                    <td><b class="tablesaw-cell-label">
                            <a href="/" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i
                                        class="fa fa-wrench"></i> </a>
                        </b>
                    </td>
                </tr>
                <tr>
                    <td><b class="tablesaw-cell-label">login_001</b></td>
                    <td><b class="tablesaw-cell-label">
                            <a href="/" class="btn btn-icon waves-effect waves-light btn-warning m-b-5"> <i
                                        class="fa fa-wrench"></i> </a>
                        </b>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
@endsection
@section('after_scripts')

@endsection
