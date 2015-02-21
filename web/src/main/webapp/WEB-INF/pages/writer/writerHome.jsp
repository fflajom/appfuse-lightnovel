<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="menu" content="Home"/>
</head>
<body class="home">


<div class="col-sm-10">
    <h2><fmt:message key="writer.heading"/></h2>

    <display:table name="books" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="books" pagesize="25" class="table table-condensed table-striped table-hover" export="true">
        <display:column property="title" escapeXml="true" sortable="true" titleKey="writer.bookTitle" style="width: 25%"
                        url="/title?from=list" paramId="id" paramProperty="id"/>

    </display:table>
</div>
</body>
