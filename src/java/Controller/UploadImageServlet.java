/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Config.ConnectionBD;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author rendo
 */
@WebServlet("/upload_image_servlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2 , //2MB
        maxFileSize = 1024 * 1024 * 10 , //10MB
        maxRequestSize = 1024 * 1024 * 50 )  //50MB
public class UploadImageServlet extends HttpServlet {
    
    private static final String UPLOAD_DIR = "images";
    
    Connection conn;
    PreparedStatement ps;
    Statement statment;
    ResultSet rs;
    
  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UploadImageServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadImageServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

  
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> imagePaths = getImagePathsFromDatabase();
        request.setAttribute ("imagePaths", imagePaths);
        
        //Redirigir al JSP que muestra las imágenes
        request.getRequestDispatcher("/views/display_images.jsp").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //obtener la ruta absoluta de la carpeta images
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        
        //Crear la carpeta images si no existe
        File uploadDir = new File (uploadFilePath);
        if (!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        
        //Obtener la imagen subida
        Part part = request.getPart("image");
        String fileName = getFileName (part);
        
        //Guardar la imagen en el servidor (en la carpeta "images")
        String filePath = uploadFilePath + File.separator + fileName;
        part.write(filePath);
        
        String relativePath = UPLOAD_DIR + File.separator + fileName;
        System.out.println("Path: "+relativePath);
        
        //Guardar la ruta de la imagen en la base de datos
        saveImagePathToDatabase(relativePath);
        
        //Responder al usuario
        response.getWriter().println("Imagen subida correctamente. Ruta: " + relativePath);
        
    }

    //  Método para obtener el nombre del archivo obtenido
    private String getFileName (Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
             if (token.trim().startsWith("filename")){
                 return token.substring(token.indexOf('=') + 2, token.length() - 1);
             }
        } 
        return "";
    }
    
    //Método para guardar la ruta de la imagen en la base de datos
    private void saveImagePathToDatabase(String imagePath) {
        try {
            ConnectionBD conexion = new ConnectionBD();
            conn = conexion.getConnectionBD();
            String sql = "INSERT INTO imagenes (ruta_imagen) VALUES (?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, imagePath);
            
            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas > 0) System.out.println("Imagen Registrada");
            else System.out.println("Imagen no Registrada");
            
            ps.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private List<String> getImagePathsFromDatabase () {
        List<String> imagePaths = new ArrayList<>();
        try {
            ConnectionBD conexion = new ConnectionBD();
            String sql = "SELECT ruta_imagen FROM imagenes";
            
            conn = conexion.getConnectionBD();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
           
            while (rs.next()){
                imagePaths.add(rs.getString("ruta_imagen"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return imagePaths;
    }
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    

}
