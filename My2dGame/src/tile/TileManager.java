package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	GamePanel gp;
	public Tile [] tile;
	public int mapTileNum [][] [];
	boolean drawPath = true;
	ArrayList<String> fileNames = new ArrayList<String>();
	ArrayList<String>	collisionStatus = new ArrayList<String>();

	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		//READ TILE DATA FILE
		
		try {
			
			InputStream is = new FileInputStream(new File("F:\\java-project-group-8\\My2dGame\\res\\maps\\tiledata.txt"));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			//GETTING TILES NAME AND COLLISION INFOR FROM TILEDATA
			String line;
			while((line  = br.readLine()) != null) {
				fileNames.add(line);
				collisionStatus.add(br.readLine());
			}
			br.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		tile = new Tile[fileNames.size()]; //10 kinds of tiles
		getTileImage();
		
		//GET THE WORLDCOL _ ROW
		try{
			InputStream is = new FileInputStream(new File("F:\\java-project-group-8\\My2dGame\\res\\maps\\worldmap (1).txt"));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String line = br.readLine();
			String mapTile[] = line.split(" ");
			gp.maxWorldCol = mapTile.length;
			gp.maxWorldRow = mapTile.length;
			mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
			
			br.close();
		}catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		}
	
		
		
		loadMap("F:\\java-project-group-8\\My2dGame\\res\\maps\\worldmap (1).txt",0);
		loadMap("F:\\java-project-group-8\\My2dGame\\res\\maps\\indoor01.txt",1);
		loadMap("F:\\java-project-group-8\\My2dGame\\res\\maps\\dungeon01.txt",2);
		loadMap("F:\\java-project-group-8\\My2dGame\\res\\maps\\dungeon02.txt",3);

	}
	
	public void setup(int index,String imagePath,boolean collision) {

		UtilityTool uTool = new UtilityTool();
		try{
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(new File("F:\\java-project-group-8\\My2dGame\\res\\tiles\\"+imagePath));
			tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getTileImage() {


		for(int i=0;i < fileNames.size();i++){
			
			String fileName;
			boolean collision;
			
			
			//getFileName
			fileName = fileNames.get(i);
			
			//GET COLLISION
			if(collisionStatus.get(i).equals("true")) {
				collision = true;
			}
			else {
				collision = false;
			}
			
			setup(i, fileName, collision);
		}
		
			
//			setup(0, "grass00", false);
//			setup(1, "grass00", false);
//			setup(2, "grass00", false);
//			setup(3, "grass00", false);
//			setup(4, "grass00", false);
//			setup(5, "grass00", false);
//			setup(6, "grass00", false);
//			setup(7, "grass00", false);
//			setup(8, "grass00", false);
//			setup(9, "grass00", false);
//			setup(10, "grass00", false);
//			setup(11, "grass01", false);
//			
//			setup(12, "water00", true);
//			setup(13, "water01", true);
//			setup(14, "water02", true);
//			setup(15, "water03", true);
//			setup(16, "water04", true);
//			setup(17, "water05", true);
//			setup(18, "water06", true);
//			setup(19, "water07", true);
//			setup(20, "water08", true);
//			setup(21, "water09", true);
//			setup(22, "water10", true);
//			setup(23, "water11", true);
//			setup(24, "water12", true);
//			setup(25, "water13", true);
//			
//			setup(26, "road00", false);
//			setup(27, "road01", false);
//			setup(28, "road02", false);
//			setup(29, "road03", false);
//			setup(30, "road04", false);
//			setup(31, "road05", false);
//			setup(32, "road06", false);
//			setup(33, "road07", false);
//			setup(34, "road08", false);
//			setup(35, "road09", false);
//			setup(36, "road10", false);
//			setup(37, "road11", false);
//			
//			setup(38, "road12", false);
//			
//			setup(39, "earth", false);
//			setup(40, "wall", true);
//			setup(41, "tree", true);
//			
//			setup(42, "hut", false);
//			setup(43, "floor01",false);
//			setup(44, "table01",true);
	}
	
	public void loadMap(String filePath,int map) {

		try {
			
			Scanner sc =  new Scanner(new File(filePath));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = sc.nextLine();
				
				while(col < gp.maxWorldCol) {
					
					String [] nums = line.split(" ");
					
					int num = Integer.parseInt(nums[col]);
					
					mapTileNum[map][col][row] = num;
					
					col++;
				}
				
				if(col == gp.maxWorldCol) {
					
					col = 0;
					
					row+=1;
					
				}
			}
			
			sc.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		int worldCol = 0;
		int worldRow = 0;
		
		while(worldCol < gp.maxWorldCol &&  worldRow < gp.maxWorldRow) {
			
			
			int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
			
			//System.out.println(worldCol +" "+ worldRow+" ");
			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			
			// transf worldPosition -> screenPosition : Player position in camera
			int screenX = worldX - gp.player.WorldX + gp.player.screenX;
			int screenY = worldY - gp.player.WorldY + gp.player.screenY;
		
				
			if(worldX + gp.tileSize > gp.player.WorldX-gp.player.screenX &&
				worldX - gp.tileSize < gp.player.WorldX +gp.player.screenX &&
				worldY + gp.tileSize > gp.player.WorldY-gp.player.screenY&&
				worldY - gp.tileSize < gp.player.WorldY + gp.player.screenY) {
				g2.drawImage(tile[tileNum].image,screenX,screenY,null);
			}
			
			worldCol++;
			
			
			if(worldCol == gp.maxWorldCol) {
				
				worldCol  = 0;
				
				worldRow++;
			
			}
		}
		if(drawPath == true) {
			g2.setColor(new Color(255,0,0,70));
			
			for(int i=0;i<gp.pFinder.pathList.size();i++) {
				
				int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
				int worldY =  gp.pFinder.pathList.get(i).row * gp.tileSize;
				
				// transf worldPosition -> screenPosition : Player position in camera
				int screenX = worldX - gp.player.WorldX + gp.player.screenX;
				int screenY = worldY - gp.player.WorldY + gp.player.screenY;
				
				g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
			}
		}
	}
}
