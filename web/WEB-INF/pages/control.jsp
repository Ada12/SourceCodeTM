<%--
  Created by IntelliJ IDEA.
  User: yangchen
  Date: 16-5-2
  Time: 下午8:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title></title>
    <style>
        /*.bubble circle{*/
        /*stroke: black;*/
        /*stroke-width: 2px;*/
        /*}*/

        .bubble text{
            fill: black;
            font-size: 14px;
            font-family: arial;
            text-anchor: middle;
        }

        .svg {
            border:1px solid #CCCCCC;
            float: right;
        }


    </style>
    <link href="/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<script src="/js/d3.min.js" charset="utf-8"></script>
<script src="/js/jquery.min.js" charset="utf-8"></script>
<script src="/js/bootstrap.min.js" charset="utf-8"></script>

<script>

    function getBubble(topicType, topicNum){
        var width  = 950;	//SVG绘制区域的宽度
        var height = 600;	//SVG绘制区域的高度
        var svg = d3.select("body")			//选择<body>
                .append("svg")			//在<body>中添加<svg>
                .attr("width", width)	//设定<svg>的宽度属性
                .attr("height", height)
                .attr("class","svg");

        //border-top:1px solid #000 stroke="blue"

        var pack = d3.layout.pack()
                .size([ width, height ])
                .sort(null)
                .value(function(d){
                    return d.weight;
                })
                .padding(2);

//        var url = window.location.href;
//        var content = url.split('/');

        d3.json("/json/topicWeight/" + topicType + "/" + topicNum,function(error, root){

            var nodes = pack.nodes(root);

            console.log(nodes);

            var color = d3.scale.category20c();

            var bubbles = svg.selectAll(".bubble")
                    .data(nodes.filter(function(d) {
                        return !d.children;
                    }))
                    .enter()
                    .append("g")
                    .attr("class","bubble")
                    .attr("onclick",function(d){
//                    return "window.location.href='/topicDetail/"+ d.name +"'";
                        return "window.location.href= 'http://localhost:8080/classTopic/"+topicType + "/" + topicNum+"'";
                    });
//                .attr("onmouseover", over())
//                .attr("onmouseout", out());

            //.append("a")
            //.attr("href","http://www.baidu.com")

            bubbles.append("circle")
                    .style("fill",function(d,i){
                        return color(i);
                    })
                    .attr("cx",function(d){ return d.x; })
                    .attr("cy",function(d){ return d.y; })
                    .attr("r",function(d){ return d.r; });

            bubbles.append("text")
                    .attr("x",function(d){ return d.x; })
                    .attr("y",function(d){ return d.y; })
                    .text(function(d){
                        return d.name;
                    });


        });
    }

    function geneBubble(){
        var topicType = $("#topicType option:selected").val();
        var topicNum = $("#topicNum").val();
        var type;
        if(topicType == "topics based on tomcat"){
            type = 1;
        }else{
            type = 0;
        }
        window.location.href = 'http://localhost:8080/topicWeight/' + type + '/' + topicNum;
    }

</script>
<div class="controlPos">
    <label for="topicType" class="control-label">Topic Type</label>
    <select class="form-control" id="topicType">
        <option>topics based on tomcat</option>
        <option>topics based on catalina</option>
    </select>
    <br/>
    <label for="topicNum" class="control-label">Topic Number</label>
    <input type="text" class="form-control" id="topicNum" placeholder="Topic Number">
    <br/>
    <button class="btn btn-primary" onclick="geneBubble()">Submit</button>
</div>

</body>
</html>
