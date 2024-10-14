<%-- 
    Document   : upload_images
    Created on : 14-oct-2024, 1:54:33
    Author     : rendo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Subir una Imagen</h2>
        <form action = "${pageContext.request.contextPath}/upload_image_servlet"
              method="post"  enctype="multipart/form-data">
            <label for="image" > Selecciona una imagen: </label>
            <input type="file"  name="image" id="image" required>
            <br> <br> 
            <button type="submit">Subir Imagen </button>
        </form>
    </body>
</html>
