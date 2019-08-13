package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture  tube1;
	Texture  tube2;
	Texture game_over;
	int flap=0;
	float bird_Y=0;
	float velocity=0;
	int game_state=0;
	float gap=400;
	Random random;
	float tube_velocity=4;
	int number_of_tubes=4;
	float tubeX[]=new float[number_of_tubes];
	float distance_between_tubes;
	float tube_offset[]=new float[number_of_tubes];
	Circle bird_circle;
	//ShapeRenderer shapeRenderer;
	Rectangle pipe_rectangle1[];
	Rectangle pipe_rectangle2[];
	int score=0;
	int score_pipe=0;

	BitmapFont font;

	public void setup(){

		bird_Y=Gdx.graphics.getHeight() / 2 - birds[flap].getHeight() / 2;

		for (int i=0;i<number_of_tubes;i++){
			tube_offset[i]=(random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight()/2 - 100); // Adjust to adjust game difficulty
			tubeX[i]=Gdx.graphics.getWidth() / 2 + 2*tube2.getWidth()  +i*distance_between_tubes;
			pipe_rectangle1[i]=new Rectangle();
			pipe_rectangle2[i]=new Rectangle();
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
        birds=new Texture[2];
        birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");


        tube1=new Texture("bottomtube.png");
		tube2=new Texture("toptube.png");
		game_over=new Texture("gover.png");

		random=new Random();


		distance_between_tubes=Gdx.graphics.getWidth()*3/4;

       // shapeRenderer=new ShapeRenderer();
		bird_circle=new Circle();
		pipe_rectangle1=new Rectangle[number_of_tubes];
		pipe_rectangle2=new Rectangle[number_of_tubes];
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		setup();




	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(tubeX[score_pipe]<Gdx.graphics.getWidth()/2) {

			score++;
			Gdx.app.log("Score:",Integer.toString(score));

			if (score_pipe < number_of_tubes-1) {
				score_pipe++;

			} else {

				score_pipe = 0;
			}
		}



		if (game_state == 1) {




				if(Gdx.input.isTouched()) {

					velocity=-10;

				}
			for (int i=0;i<number_of_tubes;i++) {

				if(tubeX[i]<-tube1.getWidth()){

					tubeX[i]+=number_of_tubes*distance_between_tubes;
					tube_offset[i]=(random.nextFloat() - 0.5f)*(Gdx.graphics.getHeight()/2 - 100); // Adjust to adjust game difficulty

				}
				tubeX[i] = tubeX[i] - tube_velocity;



				batch.draw(tube1, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tube1.getHeight() + tube_offset[i]);
				batch.draw(tube2, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tube_offset[i]);


				pipe_rectangle1[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tube1.getHeight() + tube_offset[i],tube1.getWidth(),tube1.getHeight());
				pipe_rectangle2[i]=new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tube_offset[i],tube2.getWidth(),tube2.getHeight());
			}

			if(bird_Y>0) {

					velocity = velocity + 0.5f;
					bird_Y = bird_Y - velocity;
				}else{

				game_state=2;
			}


			}
			else if(game_state==0){
				if(Gdx.input.isTouched()) {

					game_state = 1;
				}
			}

			else if(game_state==2){


                bird_Y=0;
			  	batch.draw(game_over,Gdx.graphics.getWidth()/2-game_over.getWidth()/2,Gdx.graphics.getHeight()/2-game_over.getHeight()/2);
			if(Gdx.input.isTouched()) {

				game_state=1;
				setup();
				score_pipe=0;
				score=0;
				velocity=0;


			}


			}



		if (flap == 0) {
			flap = 1;
		} else {
		    flap = 0;
		}



		batch.draw(birds[flap], Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, bird_Y);
		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()-100,Gdx.graphics.getHeight()-50);

		batch.end();




		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		bird_circle.set(Gdx.graphics.getWidth() / 2,bird_Y + (birds[flap].getHeight()/2),(birds[flap].getWidth())/2);
        //shapeRenderer.circle(bird_circle.x,bird_circle.y,bird_circle.radius);

        for(int i=0;i<number_of_tubes;i++){

        	//shapeRenderer.rect(pipe_rectangle1[i].x,pipe_rectangle1[i].y,pipe_rectangle1[i].width,pipe_rectangle1[i].height);
			//shapeRenderer.rect(pipe_rectangle2[i].x,pipe_rectangle2[i].y,pipe_rectangle2[i].width,pipe_rectangle2[i].height);

			if(Intersector.overlaps(bird_circle,pipe_rectangle1[i]) || Intersector.overlaps(bird_circle,pipe_rectangle2[i])){

                game_state=2;
			}



        }
        //shapeRenderer.end();
	}



	
	@Override
	public void dispose () {

	}
}
