package io.github.mjaroslav.mjutils.client.model.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class IconScaledWavefrontObject implements IModelCustom {
    private static final Pattern P_VERTEX = Pattern.compile(
        "(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(v( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern P_VERTEX_NORMAL = Pattern.compile(
        "(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *\\n)|(vn( (\\-){0,1}\\d+(\\.\\d+)?){3,4} *$)");
    private static final Pattern P_TEXTURE = Pattern.compile(
        "(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+(\\.\\d+)?){2,3} *$)");
    private static final Pattern P_FACE_VERTEX_TEXTURE_NORMAL = Pattern.compile(
        "(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static final Pattern P_FACE_VERTEX_TEXTURE = Pattern.compile(
        "(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static final Pattern P_FACE_VERTEX_NORMAL = Pattern.compile(
        "(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static final Pattern P_FACE_VERTEX = Pattern.compile(
        "(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static final Pattern P_GROUP_OBJECT = Pattern.compile(
        "([go]( [\\w\\d\\.]+) *\\n)|([go]( [\\w\\d\\.]+) *$)");
    private static Matcher mVertex, mVertexNormal, mTextureCoordinate, mFaceVertexTextureCoordinateNormal,
        mFaceVertexTexture, mFaceVertexNormal, mFaceVertex, mGroupObject;

    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Vertex> vertexNormals = new ArrayList<>();
    private final List<TextureCoordinate> textureCoordinates = new ArrayList<>();
    private final List<IconScaledGroupObject> groupObjects = new ArrayList<>();
    private final String fileName;
    private int glDrawingMode = -1;

    public IconScaledWavefrontObject(@NotNull ResourcePath path) throws ModelFormatException {
        this(path.getPath(), path.stream());
    }

    public IconScaledWavefrontObject(@NotNull ResourceLocation resource) throws ModelFormatException {
        this(ResourcePath.of(resource));
    }

    public IconScaledWavefrontObject(@NotNull String filename, @NotNull InputStream inputStream)
        throws ModelFormatException {
        this.fileName = filename;
        loadObjModel(inputStream);
    }

    private static @NotNull Vertex parseVertex(@NotNull IconScaledWavefrontObject model, @NotNull String line,
                                               int lineNumber) throws ModelFormatException {
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            val tokens = line.split(" ");
            try {
                if (tokens.length == 2) return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                else if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]));
                else throw new IllegalArgumentException("Vertex must be with only two or three elements");
            } catch (IllegalArgumentException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineNumber), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineNumber +
                ") in file '" + model.fileName + "' - Incorrect format");
        }
    }

    private static @NotNull Vertex parseVertexNormal(@NotNull IconScaledWavefrontObject model, @NotNull String line,
                                                     int lineNumber) throws ModelFormatException {
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            val tokens = line.split(" ");
            try {
                if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]));
                else throw new IllegalArgumentException("Vertex normal must be with only three elements");
            } catch (IllegalArgumentException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineNumber), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineNumber +
                ") in file '" + model.fileName + "' - Incorrect format");
        }
    }

    private static @NotNull TextureCoordinate parseTextureCoordinate(@NotNull IconScaledWavefrontObject model,
                                                                     @NotNull String line, int lineNumber)
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
                else throw new IllegalArgumentException("Texture coordinate must be with only two or three elements");
            } catch (IllegalArgumentException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", lineNumber), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in " +
                "file '" + model.fileName + "' - Incorrect format");
        }
    }

    private static @NotNull IconScaledFace parseFace(@NotNull IconScaledWavefrontObject model,
                                                     @NotNull IconScaledGroupObject parent, @NotNull String line,
                                                     int lineCount) throws ModelFormatException {
        if (isValidFaceLine(line)) {
            val trimmedLine = line.substring(line.indexOf(" ") + 1);
            val tokens = trimmedLine.split(" ");
            var subTokens = (String[]) null;
            if (tokens.length == 3) {
                if (model.glDrawingMode == -1) model.glDrawingMode = GL11.GL_TRIANGLES;
                else if (model.glDrawingMode != GL11.GL_TRIANGLES)
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                        + ") in file '" + model.fileName + "' - Invalid number of points for face (expected 4, found "
                        + tokens.length + ")");
            } else if (tokens.length == 4) {
                if (model.glDrawingMode == -1) model.glDrawingMode = GL11.GL_QUADS;
                else if (model.glDrawingMode != GL11.GL_QUADS)
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                        + ") in file '" + model.fileName + "' - Invalid number of points for face (expected 3, found "
                        + tokens.length + ")");
            }
            if (isValidFaceVertexTextureNormalLine(line)) { // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
                val vertices = new Vertex[tokens.length];
                val textureCoordinates = new TextureCoordinate[tokens.length];
                val vertexNormals = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    vertices[i] = model.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    textureCoordinates[i] = model.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    vertexNormals[i] = model.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }
                return new IconScaledFace(parent, vertices, vertexNormals, textureCoordinates);
            } else if (isValidFaceVertexTextureLine(line)) { // f v1/vt1 v2/vt2 v3/vt3 ...
                val vertices = new Vertex[tokens.length];
                val textureCoordinates = new TextureCoordinate[tokens.length];
                for (int i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("/");
                    vertices[i] = model.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    textureCoordinates[i] = model.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }
                return new IconScaledFace(parent, vertices, null, textureCoordinates);
            } else if (isValidFaceVertexNormalLine(line)) { // f v1//vn1 v2//vn2 v3//vn3 ...
                val vertices = new Vertex[tokens.length];
                val vertexNormals = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i) {
                    subTokens = tokens[i].split("//");
                    vertices[i] = model.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    vertexNormals[i] = model.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }
                return new IconScaledFace(parent, vertices, vertexNormals, null);
            } else if (isValidFaceVertexLine(line)) { // f v1 v2 v3 ...
                val vertices = new Vertex[tokens.length];
                for (var i = 0; i < tokens.length; ++i)
                    vertices[i] = model.vertices.get(Integer.parseInt(tokens[i]) - 1);
                return new IconScaledFace(parent, vertices, null, null);
            } else
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                    + ") in file '" + model.fileName + "' - Incorrect format");
        } else
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount
                + ") in file '" + model.fileName + "' - Incorrect format");
    }

    private static @NotNull IconScaledGroupObject parseGroupObject(@NotNull IconScaledWavefrontObject model,
                                                                   @NotNull String line, int lineCount)
        throws ModelFormatException {
        if (isValidGroupObjectLine(line)) {
            val trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (!trimmedLine.isEmpty()) return new IconScaledGroupObject(model, trimmedLine);
            else
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount +
                    ") in file '" + model.fileName + "' - Incorrect format");
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '"
                + model.fileName + "' - Incorrect format");
        }
    }

    private static boolean isValidVertexLine(@NotNull String line) {
        if (mVertex != null) mVertex.reset(line);
        else mVertex = P_VERTEX.matcher(line);
        return mVertex.matches();
    }

    private static boolean isValidVertexNormalLine(@NotNull String line) {
        if (mVertexNormal != null) mVertexNormal.reset(line);
        else mVertexNormal = P_VERTEX_NORMAL.matcher(line);
        return mVertexNormal.matches();
    }

    private static boolean isValidTextureCoordinateLine(@NotNull String line) {
        if (mTextureCoordinate != null) mTextureCoordinate.reset(line);
        else mTextureCoordinate = P_TEXTURE.matcher(line);
        return mTextureCoordinate.matches();
    }

    private static boolean isValidFaceVertexTextureNormalLine(@NotNull String line) {
        if (mFaceVertexTextureCoordinateNormal != null) mFaceVertexTextureCoordinateNormal.reset(line);
        else mFaceVertexTextureCoordinateNormal = P_FACE_VERTEX_TEXTURE_NORMAL.matcher(line);
        return mFaceVertexTextureCoordinateNormal.matches();
    }

    private static boolean isValidFaceVertexTextureLine(@NotNull String line) {
        if (mFaceVertexTexture != null) mFaceVertexTexture.reset(line);
        else mFaceVertexTexture = P_FACE_VERTEX_TEXTURE.matcher(line);
        return mFaceVertexTexture.matches();
    }

    private static boolean isValidFaceVertexNormalLine(@NotNull String line) {
        if (mFaceVertexNormal != null) mFaceVertexNormal.reset(line);
        else mFaceVertexNormal = P_FACE_VERTEX_NORMAL.matcher(line);
        return mFaceVertexNormal.matches();
    }

    private static boolean isValidFaceVertexLine(@NotNull String line) {
        if (mFaceVertex != null) mFaceVertex.reset(line);
        else mFaceVertex = P_FACE_VERTEX.matcher(line);
        return mFaceVertex.matches();
    }

    private static boolean isValidFaceLine(@NotNull String line) {
        return isValidFaceVertexTextureNormalLine(line) || isValidFaceVertexTextureLine(line) ||
            isValidFaceVertexNormalLine(line) || isValidFaceVertexLine(line);
    }

    private static boolean isValidGroupObjectLine(@NotNull String line) {
        if (mGroupObject != null) mGroupObject.reset(line);
        else mGroupObject = P_GROUP_OBJECT.matcher(line);
        mGroupObject.reset(line);
        return mGroupObject.matches();
    }

    protected void loadObjModel(@NotNull InputStream inputStream) throws ModelFormatException {
        var currentLine = "";
        var lineNumber = 0;
        try (inputStream; val reader = new BufferedReader(new InputStreamReader(inputStream))) {
            var currentGroupObject = (IconScaledGroupObject) null;
            while ((currentLine = reader.readLine()) != null) {
                lineNumber++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();
                if (currentLine.startsWith("v ")) {
                    vertices.add(parseVertex(this, currentLine, lineNumber));
                } else if (currentLine.startsWith("vn ")) {
                    vertexNormals.add(parseVertexNormal(this, currentLine, lineNumber));
                } else if (currentLine.startsWith("vt ")) {
                    textureCoordinates.add(parseTextureCoordinate(this, currentLine, lineNumber));
                } else if (currentLine.startsWith("f ")) {
                    if (currentGroupObject == null) currentGroupObject = new IconScaledGroupObject(this);
                    currentGroupObject.getFaces().add(parseFace(this, currentGroupObject, currentLine, lineNumber));
                } else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                    val group = parseGroupObject(this, currentLine, lineNumber);
                    if (currentGroupObject != null) groupObjects.add(currentGroupObject);
                    currentGroupObject = group;
                }
            }
            groupObjects.add(currentGroupObject);
        } catch (IOException e) {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(@NotNull Tessellator tessellator) {
        for (val groupObject : groupObjects) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(@NotNull IIcon icon, @NotNull Tessellator tessellator) {
        for (val groupObject : groupObjects) groupObject.render(icon, tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(@NotNull Tessellator tessellator, @NotNull String... groupNames) {
        for (val groupObject : groupObjects)
            for (val groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.getName())) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(@NotNull IIcon icon, @NotNull Tessellator tessellator, @NotNull String... groupNames) {
        for (val groupObject : groupObjects)
            for (val groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.getName())) groupObject.render(icon, tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(@NotNull Tessellator tessellator, @NotNull String partName) {
        for (val groupObject : groupObjects)
            if (partName.equalsIgnoreCase(groupObject.getName())) groupObject.render(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(@NotNull IIcon icon, @NotNull Tessellator tessellator, @NotNull String partName) {
        for (val groupObject : groupObjects)
            if (partName.equalsIgnoreCase(groupObject.getName())) groupObject.render(icon, tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(@NotNull Tessellator tessellator, @NotNull String... excludedGroupNames) {
        var exclude = false;
        for (val groupObject : groupObjects) {
            exclude = false;
            for (val excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.getName())) {
                    exclude = true;
                    break;
                }
            if (!exclude) groupObject.render(tessellator);
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(@NotNull IIcon icon, @NotNull Tessellator tessellator,
                                    @NotNull String... excludedGroupNames) {
        var exclude = false;
        for (val groupObject : groupObjects) {
            exclude = false;
            for (val excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.getName())) {
                    exclude = true;
                    break;
                }
            if (!exclude) groupObject.render(icon, tessellator);
        }
    }

    @Override
    public String getType() {
        return "obj";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderAll() {
        val tessellator = Tessellator.instance;
        val mode = getGlDrawingMode();
        tessellator.startDrawing(mode == -1 ? GL11.GL_TRIANGLES : mode);
        tessellateAll(tessellator);
        tessellator.draw();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnly(@NotNull String... groupNames) {
        for (val groupObject : groupObjects)
            for (val groupName : groupNames)
                if (groupName.equalsIgnoreCase(groupObject.getName())) groupObject.render();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderPart(@NotNull String partName) {
        for (val groupObject : groupObjects) if (partName.equalsIgnoreCase(groupObject.getName())) groupObject.render();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderAllExcept(@NotNull String... excludedGroupNames) {
        var skipPart = false;
        for (val groupObject : groupObjects) {
            for (val excludedGroupName : excludedGroupNames)
                if (excludedGroupName.equalsIgnoreCase(groupObject.getName())) {
                    skipPart = true;
                    break;
                }
            if (!skipPart) groupObject.render();
        }
    }
}
