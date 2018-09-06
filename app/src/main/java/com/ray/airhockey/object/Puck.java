package com.ray.airhockey.object;

import com.ray.airhockey.data.VertexArray;
import com.ray.airhockey.program.ColorShaderProgram;

import java.util.List;

import static com.ray.airhockey.object.ObjectBuilder.DrawCommand;
import static com.ray.airhockey.object.ObjectBuilder.GeneratedData;
import static com.ray.airhockey.util.Geometry.Cylinder;
import static com.ray.airhockey.util.Geometry.Point;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-06 14:50
 *  description : 
 */
public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float mRadius, mHeight;

    private final VertexArray mVertexArray;

    private final List<DrawCommand> mDrawCommandList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        GeneratedData generatedData = ObjectBuilder.createPuck(
                new Cylinder(new Point(0f, 0f, 0f), radius, height),
                numPointsAroundPuck);
        mRadius = radius;
        mHeight = height;
        mVertexArray = new VertexArray(generatedData.mVertexData);
        mDrawCommandList = generatedData.mDrawCommands;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw(){
        for (DrawCommand drawCommand : mDrawCommandList) {
            drawCommand.draw();
        }
    }

}
