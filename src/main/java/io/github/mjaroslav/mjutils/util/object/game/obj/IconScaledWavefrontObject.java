package io.github.mjaroslav.mjutils.util.object.game.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Just wrapper for {@link net.minecraftforge.client.model.obj.WavefrontObject WavefrontObject} for using with
 * {@link IIcon IIcon}.
 *
 * @see net.minecraftforge.client.model.obj.WavefrontObject WavefrontObject
 */
public class IconScaledWavefrontObject implements IModelCustom {
    public static final Pattern vertexPattern = Pattern.compile(
        "(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    public static final Pattern vertexNormalPattern = Pattern.compile(
        "(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    public static final Pattern textureCoordinatePattern = Pattern.compile(
        "(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    public static final Pattern face_V_VT_VN_Pattern = Pattern.compile(
        "(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    public static final Pattern face_V_VT_Pattern = Pattern.compile(
        "(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    public static final Pattern face_V_VN_Pattern = Pattern.compile(
        "(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    public static final Pattern face_V_Pattern = Pattern.compile(
        "(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    public static final Pattern groupObjectPattern = Pattern.compile(
        "([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");

    public static Matcher vertexMatcher, vertexNormalMatcher, textureCoordinateMatcher;
    public static Matcher face_V_VT_VN_Matcher, face_V_VT_Matcher, face_V_VN_Matcher, face_V_Matcher;
    public static Matcher groupObjectMatcher;

    public ArrayList<Vertex> vertices = new ArrayList<>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<>();
    public ArrayList<IconScaledGroupObject> groupObjects = new ArrayList<>();
    protected IconScaledGroupObject currentGroupObject;
    protected final String fileName;

    public IconScaledWavefrontObject(@NotNull ResourcePath path) {
        this.fileName = path.getPath();
        try {
            loadObjModel(path.stream());
        } catch (Exception e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    public IconScaledWavefrontObject(@NotNull ResourceLocation resource) throws ModelFormatException {
        this.fileName = resource.toString();

        try {
            IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
            loadObjModel(res.getInputStream());
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    public IconScaledWavefrontObject(@NotNull String filename, @NotNull InputStream inputStream)
        throws ModelFormatException {
        this.fileName = filename;
        loadObjModel(inputStream);
    }

    protected void loadObjModel(@NotNull InputStream inputStream) throws ModelFormatException {
        String currentLine;
        int lineCount = 0;
        try (inputStream; BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((currentLine = reader.readLine()) != null) {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (currentLine.startsWith("v ")) {
                    val vertex = parseVertex(currentLine, lineCount);
                    if (vertex != null) vertices.add(vertex);
                } else if (currentLine.startsWith("vn ")) {
                    val vertex = parseVertexNormal(currentLine, lineCount);
                    if (vertex != null) vertexNormals.add(vertex);
                } else if (currentLine.startsWith("vt ")) {
                    val textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate != null) textureCoordinates.add(textureCoordinate);
                } else if (currentLine.startsWith("f ")) {
                    if (currentGroupObject == null) currentGroupObject = new IconScaledGroupObject("Default");
                    val face = parseFace(currentLine, lineCount);
                    currentGroupObject.faces.add(face);
                } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                    val group = parseGroupObject(currentLine, lineCount);
                    if (group != null && currentGroupObject != null) groupObjects.add(currentGroupObject);
                    currentGroupObject = group;
                }
            }
            groupObjects.add(currentGroupObject);
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAll() {
        val tessellator = Tessellator.instance;
        if (currentGroupObject != null) tessellator.startDrawing(currentGroupObject.glDrawingMode);
        else tessellator.startDrawing(GL11.GL_TRIANGLES);
        tessellateAll(tessellator);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(@NotNull Tessellator tessellator) {
        for (var groupObject : groupObjects) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(@NotNull IIcon icon, @NotNull Tessellator tessellator) {
        for (var groupObject : groupObjects) groupObject.render(icon, tessellator);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnly(@NotNull String... groupNames) {
        for (var groupObject : groupObjects)
            for (var groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.name)) groupObject.render();
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(@NotNull Tessellator tessellator, @NotNull String... groupNames) {
        for (var groupObject : groupObjects)
            for (var groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.name)) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(@NotNull IIcon icon, @NotNull Tessellator tessellator, @NotNull String... groupNames) {
        for (var groupObject : groupObjects)
            for (var groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.name)) groupObject.render(icon, tessellator);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderPart(@NotNull String partName) {
        for (var groupObject : groupObjects) if (partName.equalsIgnoreCase(groupObject.name)) groupObject.render();
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(@NotNull Tessellator tessellator, @NotNull String partName) {
        for (var groupObject : groupObjects)
            if (partName.equalsIgnoreCase(groupObject.name)) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(@NotNull IIcon icon, @NotNull Tessellator tessellator, @NotNull String partName) {
        for (var groupObject : groupObjects)
            if (partName.equalsIgnoreCase(groupObject.name)) groupObject.render(icon, tessellator);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAllExcept(@NotNull String... excludedGroupNames) {
        for (var groupObject : groupObjects) {
            boolean skipPart = false;
            for (var excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
                    skipPart = true;
                    break;
                }
            if (!skipPart) groupObject.render();
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(@NotNull Tessellator tessellator, @NotNull String... excludedGroupNames) {
        boolean exclude;
        for (var groupObject : groupObjects) {
            exclude = false;
            for (var excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
                    exclude = true;
                    break;
                }
            if (!exclude) groupObject.render(tessellator);
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(@NotNull IIcon icon, @NotNull Tessellator tessellator,
                                    @NotNull String... excludedGroupNames) {
        boolean exclude;
        for (var groupObject : groupObjects) {
            exclude = false;
            for (var excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
                    exclude = true;
                    break;
                }
            if (!exclude) groupObject.render(icon, tessellator);
        }
    }

    protected @Nullable Vertex parseVertex(@NotNull String line, int lineCount) throws ModelFormatException {
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            val tokens = line.split(" ");
            try {
                if (tokens.length == 2) return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                else if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                + fileName + "' - Incorrect format");
        }
        return null;
    }

    protected @Nullable Vertex parseVertexNormal(@NotNull String line, int lineCount) throws ModelFormatException {
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            val tokens = line.split(" ");
            try {
                if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                + fileName + "' - Incorrect format");
        }
        return null;
    }

    protected @Nullable TextureCoordinate parseTextureCoordinate(@NotNull String line, int lineCount)
        throws ModelFormatException {
        if (isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            val tokens = line.split(" ");
            try {
                if (tokens.length == 2)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
                else if (tokens.length == 3)
                    return new TextureCoordinate(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                + fileName + "' - Incorrect format");
        }
        return null;
    }

    protected @Nullable IconScaledFace parseFace(@NotNull String line, int lineCount) throws ModelFormatException {
        IconScaledFace face;
        if (isValidFaceLine(line)) {
            face = new IconScaledFace();
            val trimmedLine = line.substring(line.indexOf(" ") + 1);
            val tokens = trimmedLine.split(" ");
            String[] subTokens;
            if (tokens.length == 3) {
                if (currentGroupObject.glDrawingMode == -1) currentGroupObject.glDrawingMode = GL11.GL_TRIANGLES;
                else if (currentGroupObject.glDrawingMode != GL11.GL_TRIANGLES)
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                        + ") in file '" + fileName + "' - Invalid number of points for face (expected 4, found "
                        + tokens.length + ")");
            } else if (tokens.length == 4) {
                if (currentGroupObject.glDrawingMode == -1) currentGroupObject.glDrawingMode = GL11.GL_QUADS;
                else if (currentGroupObject.glDrawingMode != GL11.GL_QUADS)
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                        + ") in file '" + fileName + "' - Invalid number of points for face (expected 3, found "
                        + tokens.length + ")");
            }
            // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
            if (isValidFace_V_VT_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1/vt1 v2/vt2 v3/vt3 ...
            else if (isValidFace_V_VT_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1//vn1 v2//vn2 v3//vn3 ...
            else if (isValidFace_V_VN_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");
                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }
                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1 v2 v3 ...
            else if (isValidFace_V_Line(line)) {
                face.vertices = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i)
                    face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
                face.faceNormal = face.calculateFaceNormal();
            } else {
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                    + ") in file '" + fileName + "' - Incorrect format");
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                + ") in file '" + fileName + "' - Incorrect format");
        }
        return face;
    }

    protected @Nullable IconScaledGroupObject parseGroupObject(@NotNull String line, int lineCount)
        throws ModelFormatException {
        IconScaledGroupObject group = null;
        if (isValidGroupObjectLine(line)) {
            val trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (trimmedLine.length() > 0) group = new IconScaledGroupObject(trimmedLine);
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                + fileName + "' - Incorrect format");
        }
        return group;
    }

    /***
     * Verifies that the given line from the model file is a valid vertex
     * @param line the line being validated
     * @return true if the line is a valid vertex, false otherwise
     */
    protected static boolean isValidVertexLine(@NotNull String line) {
        if (vertexMatcher != null) vertexMatcher.reset();
        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid vertex normal
     * @param line the line being validated
     * @return true if the line is a valid vertex normal, false otherwise
     */
    protected static boolean isValidVertexNormalLine(@NotNull String line) {
        if (vertexNormalMatcher != null) vertexNormalMatcher.reset();
        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid texture coordinate
     * @param line the line being validated
     * @return true if the line is a valid texture coordinate, false otherwise
     */
    protected static boolean isValidTextureCoordinateLine(@NotNull String line) {
        if (textureCoordinateMatcher != null) textureCoordinateMatcher.reset();
        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices, texture
     * coordinates, and vertex normals
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1/vt1/vn1 ..." (with a minimum of 3
     * points in the face, and a maximum of 4), false otherwise
     */
    protected static boolean isValidFace_V_VT_VN_Line(@NotNull String line) {
        if (face_V_VT_VN_Matcher != null) face_V_VT_VN_Matcher.reset();
        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices and texture
     * coordinates
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1/vt1 ..." (with a minimum of 3 points
     * in the face, and a maximum of 4), false otherwise
     */
    protected static boolean isValidFace_V_VT_Line(@NotNull String line) {
        if (face_V_VT_Matcher != null) face_V_VT_Matcher.reset();
        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices and vertex normals
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1//vn1 ..." (with a minimum of 3 points
     * in the face, and a maximum of 4), false otherwise
     */
    protected static boolean isValidFace_V_VN_Line(@NotNull String line) {
        if (face_V_VN_Matcher != null) face_V_VN_Matcher.reset();
        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by only vertices
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1 ..." (with a minimum of 3 points in
     * the face, and a maximum of 4), false otherwise
     */
    protected static boolean isValidFace_V_Line(@NotNull String line) {
        if (face_V_Matcher != null) face_V_Matcher.reset();
        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face of any of the possible face formats
     * @param line the line being validated
     * @return true if the line is a valid face that matches any of the valid face formats, false otherwise
     */
    protected static boolean isValidFaceLine(@NotNull String line) {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line)
            || isValidFace_V_Line(line);
    }

    /***
     * Verifies that the given line from the model file is a valid group (or object)
     * @param line the line being validated
     * @return true if the line is a valid group (or object), false otherwise
     */
    protected static boolean isValidGroupObjectLine(@NotNull String line) {
        if (groupObjectMatcher != null) groupObjectMatcher.reset();
        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    @Override
    public String getType() {
        return "obj";
    }
}
