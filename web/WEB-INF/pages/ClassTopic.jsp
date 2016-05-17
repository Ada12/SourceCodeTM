<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 16-5-6
  Time: 上午11:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link href="/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="/css/main.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        function getResult() {
            var url = window.location.href;
            var content = url.split('/');
            var urlSend = "/json/classTopic/" + content[4] + "/" + content[5];
            var $f = $('<th>#</th>');
            $f.appendTo( $("#myHead") );
            for(var i = 0; i < Number(content[5]); i ++){
                var $head = $('<th>'+ i +'</th>');
                $head.appendTo( $("#myHead"));
            }
            $.get(urlSend, {Action:"get",Name:"yc"}, function (response, textStatus){
                var detail = response.result;
                for(var j = 0; j < detail.length; j ++){
                    var $tr = $('<tr id="tr'+ j+ '"></tr>');
                    $tr.appendTo($("#myBody"));
                    var $th = $('<th scope="row">'+ detail[j]["name"].trim() +'</th>');
                    $th.appendTo($("#tr" + j));
                    var c = detail[j]["content"].split("-");
                    for(var k = 0; k < c.length-1; k ++){
                        var $td;
                        if(c[k] == "1"){
                            $td = $('<td><span class="glyphicon glyphicon-ok hColor" aria-hidden="true"></span></td>');
                        }else if(c[k] == "2"){
                            $td = $('<td><span class="glyphicon glyphicon-ok cColor" aria-hidden="true"></span></td>');
                        }else{
                            $td = $('<td><span class="glyphicon glyphicon-ok lColor" aria-hidden="true"></span></td>');
                        }
                        $td.appendTo($("#tr" + j));
                    }
                }

            })
        }

        function getNewResult(){
            var url = window.location.href;
            var content = url.split('/');
            var urlSend = "/json/classTopic/" + content[4] + "/" + content[5];

            $.get(urlSend, {Action:"get",Name:"yc"}, function (response, textStatus){
                var detail = response.result;
                for(var i = 0; i < detail.length; i ++) {
                    var $panel = $('<div class="panel panel-default"> \
                <div class="panel-heading" role="tab" id="heading'+ i +'"> \
                <h4 class="panel-title"> \
                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse'+ i +'" aria-controls="collapse'+ i +'"> \
                '+ detail[i]["module"] +' \
                </a> \
                </h4> \
                </div> \
                <div id="collapse'+ i +'" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading'+ i +'"> \
                <div class="panel-body"> \
                <table class="table table-bordered table-striped"> \
                    <thead> \
                    <tr id="'+ detail[i]["module"] +'_head"> \
                    </tr> \
                    </thead> \
                    <tbody id="'+ detail[i]["module"] +'_body"> \
                    </tbody> \
                    </table> \
                </div> \
                </div> \
                </div>');
                $panel.appendTo($("#accordion"));
                }

                for(var m = 0; m < detail.length; m ++) {

                    //记得 10.60.42.62:8001

                    var $f = $('<th><a href="http://localhost:8080/stackMWE/'+ content[4] + '/' + content[5] + '/' + detail[m]["module"] +'"><span class="glyphicon glyphicon-tasks iconColor" aria-hidden="true"></span></a></th>');
                    $f.appendTo($("#" + detail[m]["module"] + "_head"));

                    for(var n = 0; n < Number(content[5]); n ++){
                        var $head = $('<th>'+ n +'</th>');
                        $head.appendTo($("#" + detail[m]["module"] + "_head"));
                    }

                    for (var j = 0; j < detail[m]["classes"].length; j++) {

                        var $tr = $('<tr id= "'+ detail[m]["module"] +'_tr' + j + '"></tr>');
                        $tr.appendTo($("#" + detail[m]["module"] + "_body"));

                        var $th = $('<th scope="row">' + detail[m]["classes"][j]["name"].trim() + '</th>');
                        $th.appendTo($("#" + detail[m]["module"] + "_tr" + j));
                        var c = detail[m]["classes"][j]["content"].split("-");
                        for (var k = 0; k < c.length - 1; k++) {
                            var $td;
                            if (c[k] == "1") {
                                $td = $('<td><span class="glyphicon glyphicon-ok hColor" aria-hidden="true"></span></td>');
                            } else if (c[k] == "2") {
                                $td = $('<td><span class="glyphicon glyphicon-ok cColor" aria-hidden="true"></span></td>');
                            } else {
                                $td = $('<td><span class="glyphicon glyphicon-ok lColor" aria-hidden="true"></span></td>');
                            }
                            $td.appendTo($("#" + detail[m]["module"] + "_tr" + j));
                        }
                    }
                }

            })
        }

    </script>
</head>
<body onload="getNewResult()">
<script src="/js/d3.min.js" charset="utf-8"></script>
<script src="/js/jquery.min.js" charset="utf-8"></script>
<script src="/js/bootstrap.min.js" charset="utf-8"></script>
<%--<div class="tablePos">--%>
    <%--<table class="table table-bordered table-striped">--%>
        <%--<thead>--%>
        <%--<tr id="myHead">--%>
        <%--</tr>--%>
        <%--</thead>--%>
        <%--<tbody id="myBody">--%>
        <%--&lt;%&ndash;<tr>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<th scope="row">3</th>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<td><span class="glyphicon glyphicon-ok iconColor" aria-hidden="true"></span></td>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<td><span class="glyphicon glyphicon-ok cColor" aria-hidden="true"></span></td>&ndash;%&gt;--%>
            <%--&lt;%&ndash;<td><span class="glyphicon glyphicon-ok lColor" aria-hidden="true"></span></td>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</tr>&ndash;%&gt;--%>
        <%--</tbody>--%>
    <%--</table>--%>
<%--</div>--%>

<div class="ntablePos">
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true"></div>
</div>
</body>
</html>
