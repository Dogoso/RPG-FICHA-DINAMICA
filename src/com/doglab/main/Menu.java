package com.doglab.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.doglab.entities.AtributosLabel;
import com.doglab.entities.CharacterIcon;
import com.doglab.entities.CharacterLabel;
import com.doglab.entities.CheckBox;
import com.doglab.entities.CombatLabel;
import com.doglab.entities.DetailsLabel;
import com.doglab.entities.Dice;
import com.doglab.entities.Entity;
import com.doglab.entities.FastSkillsLabel;
import com.doglab.entities.GunLabel;
import com.doglab.entities.IconLabel;
import com.doglab.entities.InventoryLabel;
import com.doglab.entities.ItemLabel;
import com.doglab.entities.OptionsLabel;
import com.doglab.entities.ReadmeLabel;
import com.doglab.entities.Rituals;
import com.doglab.entities.RitualsLabel;
import com.doglab.entities.SkillLabel;
import com.doglab.entities.Skills;
import com.doglab.entities.SquareTextLabel;
import com.doglab.entities.StatsLabel;
import com.doglab.entities.TextLabel;
import com.doglab.menu.HomeButton;

public class Menu {

	public static String current = "info.txt";
	
	private final Color BLACK = new Color(0xFF000000);
	private Color bg = BLACK;
	public static Font specialElite;
	private BufferedImage icon;
	
	public static boolean showReadme = true;
	private int x, y;
	
	public Menu(int x, int y) {
		try {
			specialElite = Font.createFont(Font.TRUETYPE_FONT, new File("res/SpecialElite-Regular.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/SpecialElite-Regular.ttf")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DetailsLabel detailsLabel = new DetailsLabel(30, 100, Game.WIDTH/2-50, 370, 0, null);
		Game.entities.add(detailsLabel);
		IconLabel iconLabel = new IconLabel(346, 80, 285, 160, 0, null);
		Game.entities.add(iconLabel);
		StatsLabel statsLabel = new StatsLabel(340, 250,(int)(Game.WIDTH/2.18), 440, 0, null);
		Game.entities.add(statsLabel);
		TextLabel title = new TextLabel(240, 60, 200, 29, 0, null, specialElite.deriveFont(29.0f), 
				new Color(0xFFE8EDEB), "Perfil do Jogador", 1, false);
		Game.entities.add(title);
		AtributosLabel atrLabel = new AtributosLabel(25, 550, 300, 480, 0, null);
		Game.entities.add(atrLabel);
		FastSkillsLabel fastSLabel = new FastSkillsLabel(340, 550, 320, 100, 0, null);
		Game.entities.add(fastSLabel);
		CharacterLabel characterLabel = new CharacterLabel(340, 820, 320, 210, 0, null);
		Game.entities.add(characterLabel);
		CombatLabel combatLabel = new CombatLabel(10, 1050, Game.WIDTH*Game.SCALE-30, 200, 0, null);
		Game.entities.add(combatLabel);
		Rituals rituals = new Rituals(10, 1265, Game.WIDTH*Game.SCALE-30, 400, 0, null);
		Game.entities.add(rituals);
		int down = 410;
		Skills skills = new Skills(10, 1270+down, Game.WIDTH*Game.SCALE-30, 600, 0, null);
		Game.entities.add(skills);
		InventoryLabel inventory = new InventoryLabel(10, 1890+down, Game.WIDTH*Game.SCALE-30, 300, 0, null);
		Game.entities.add(inventory);
		OptionsLabel optionsLabel = new OptionsLabel(0, 0-45, Game.WIDTH, 45, 0, Game.spr_entities.getSprite(26, 231, 25, 25));
		Game.entities.add(optionsLabel);
		try {
			icon = ImageIO.read(getClass().getResource("/dogcon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadSave(loadGame());
		if(showReadme) {
			ReadmeLabel rL = new ReadmeLabel(getX()+30, getY()+30, Game.WIDTH*Game.SCALE-70, Game.HEIGHT*Game.SCALE-40, 0, null);
			Game.entities.add(rL);
		}
	}

	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(bg);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.drawImage(icon, 230, 2610-Game.roller.getY()*Game.roller.step, null);
	}	
	
	public static void saveGame(String line) {
		File file = new File(current);
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter(current));
		}catch(IOException e) {
			e.printStackTrace();
		}
		try {
			write.write(line);
			write.newLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		try {
			// Aparecer na tela do topo que o jogo est� sendo salvo.
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof OptionsLabel) {
					((OptionsLabel) e).saving = true;
				}
			}
			String finalDet = "";
			String bigger = "/";
			String medium = "%";
			String little = "<>";
			for(Entity e : Game.entities) {
				if(e instanceof DetailsLabel) { // Label de detalhes
					String details = "Details"+medium;
					String detailsX = "DetailsX"+medium;
					for(Entity l : ((DetailsLabel) e).labels) {
						details += ((TextLabel) l).text;
						detailsX+= Integer.toString(l.getX());
						details+=medium;
						detailsX+=medium;
					}
					finalDet+=details;
					finalDet+=bigger;
					finalDet+=detailsX;
					finalDet+=bigger;
				}else if(e instanceof StatsLabel) {
					String stats = "Stats"+medium;
					String statsX = "StatsX"+medium;
					for(Entity l : ((StatsLabel) e).labels) {
						if(l instanceof TextLabel) {
							stats+=((TextLabel) l).text;
							statsX+=Integer.toString(l.getX());
							stats+=medium;
							statsX+=medium;
						}else if(l instanceof CheckBox) {
							stats+=((CheckBox) l).getArrayList().get(0).text;
							stats+=little;
							stats+=Boolean.toString(((CheckBox) l).getChecked());
							statsX+=Integer.toString(l.getX());
							stats+=medium;
							statsX+=medium;
						}
					}
					finalDet+=stats;
					finalDet+=bigger;
					finalDet+=statsX;
					finalDet+=bigger;
				}else if(e instanceof AtributosLabel) {
					String atr = "Atributes"+medium;
					String atrX = "AtributesX"+medium;
					for(Entity l : ((AtributosLabel) e).labels) {
						if(l instanceof TextLabel) {
							atr+=((TextLabel) l).text;
							atrX+=Integer.toString(l.getX());
							atr+=medium;
							atrX+=medium;
						}
					}
					finalDet+=atr;
					finalDet+=bigger;
					finalDet+=atrX;
					finalDet+=bigger;
				}else if(e instanceof IconLabel) {
					finalDet+="Icon"+medium;
					finalDet+=((CharacterIcon) ((IconLabel) e).labels.get(0)).path;
					finalDet+=bigger;
				}else if(e instanceof FastSkillsLabel){
					String fstSK = "FastSkillsLabel"+medium;
					String fstSKX = "FastSkillsLabelX"+medium;
					String fstSKK = "FastSkills"+medium;
					for(Entity l : ((FastSkillsLabel) e).labels) {
						if(l instanceof TextLabel) {
							fstSK+=((TextLabel) l).text;
							fstSKX+=((TextLabel) l).getX();
							fstSK+=medium;
							fstSKX+=medium;
						}
					}
					for(Entity l : ((FastSkillsLabel) e).getSkillArrayList()) {
						fstSKK+=((SkillLabel) l).getID();
						fstSKK+=medium;
					}
					finalDet+=fstSK;
					finalDet+=bigger;
					finalDet+=fstSKX;
					finalDet+=bigger;
					finalDet+=fstSKK;
					finalDet+=bigger;
				}else if(e instanceof CharacterLabel) {
					String paths = "CharacterLabel"+medium;
					for(String s : ((CharacterLabel) e).getPaths()) {
						paths+=s;
						paths+=medium;
					}
					finalDet+=paths;
					finalDet+=bigger;
				}else if(e instanceof CombatLabel) {
					String combat = "CombatLabel"+medium;
					String combatX = "CombatLabelX"+medium;
					String size = Integer.toString(((CombatLabel) e).getGunArrayList().size());
					String guns = "Guns"+medium+size+medium;
					for(Entity l : ((CombatLabel) e).labels) {
						if(l instanceof TextLabel) {
							combat+=((TextLabel) l).text;
							combatX+=((TextLabel) l).getX();
							combat+=medium;
							combatX+=medium;
						}
					}
					for(GunLabel g : ((CombatLabel) e).getGunArrayList()) {
						for(Entity t : g.labels) {
							if(t instanceof TextLabel) {
								guns+=((TextLabel) t).text;
								guns+=medium;
							}
						}
					}
					finalDet+=combat;
					finalDet+=bigger;
					finalDet+=combatX;
					finalDet+=bigger;
					finalDet+=guns;
					finalDet+=bigger;
				}else if(e instanceof Rituals) {
					String rituals = "RitualsLabel"+medium;
					String ritualsX = "RitualsLabelX"+medium;
					String size = Integer.toString(((Rituals) e).getRituals().size());
					String rit = "Rituals"+medium+size+medium;
					for(Entity l : ((Rituals) e).labels) {
						if(l instanceof TextLabel) {
							rituals+=((TextLabel) l).text;
							ritualsX+=((TextLabel) l).getX();
							rituals+=medium;
							ritualsX+=medium;
						}
					}
					for(RitualsLabel r : ((Rituals) e).getRituals()) {
						for(int ii = 0; ii < r.labels.size(); ii++) {
							Entity t = r.labels.get(ii);
							if(t instanceof TextLabel) {
								rit+=((TextLabel) t).text;
								rit+=medium;
							}
							if(!(ii+1<r.labels.size())) {
								rit+=r.path;
								rit+=medium;
							}
						}
					}
					finalDet+=rituals;
					finalDet+=bigger;
					finalDet+=ritualsX;
					finalDet+=bigger;
					finalDet+=rit;
					finalDet+=bigger;
				}else if(e instanceof Skills) {
					String skills = "Skills"+medium;
					String skillsX = "SkillsX"+medium;
					String skillsRoll = "SkillsRoll"+medium;
					for(int ii = 1; ii < ((Skills) e).labels.size(); ii++) {
						Entity l = ((Skills) e).labels.get(ii);
						if(l instanceof TextLabel) {
							skills+=((TextLabel) l).text;
							skillsX+=((TextLabel) l).getX();
							skills+=medium;
							skillsX+=medium;
						}
					}
					for(SquareTextLabel s : ((Skills) e).getSkills()) {
						for(int ii = 0; ii < s.labels.size(); ii++) {
							Entity t = s.labels.get(ii);
							if(t instanceof TextLabel) {
								skillsRoll+=((TextLabel) t).text;
								skillsRoll+=little;
							}
						}
						skillsRoll+=medium;
					}
					finalDet+=skills;
					finalDet+=bigger;
					finalDet+=skillsX;
					finalDet+=bigger;
					finalDet+=skillsRoll;
					finalDet+=bigger;
				}else if(e instanceof InventoryLabel) {
					String inventory = "Inventory"+medium;
					String itens = "Itens"+medium;
					for(int ii = 0; ii < ((InventoryLabel) e).labels.size(); ii++) {
						Entity l = ((InventoryLabel) e).labels.get(ii);
						if(l instanceof TextLabel) {
							inventory+=((TextLabel) l).text;
							inventory+=medium;
						}
					}
					for(ItemLabel il : ((InventoryLabel) e).getItemArrayList()) {
						for(int ii = 0; ii < il.labels.size(); ii++) {
							Entity t = il.labels.get(ii);
							if(t instanceof TextLabel) {
								itens+=((TextLabel) t).text;
								itens+=little;
							}
						}
						itens+=medium;
					}
					finalDet+=inventory;
					finalDet+=bigger;
					finalDet+=bigger;
					finalDet+=itens;
					finalDet+=bigger;
				}
			}
			saveGame(finalDet);
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof OptionsLabel) {
				((OptionsLabel) e).saving = false;
				((OptionsLabel) e).saved = true;
			}
		}
	}

	public static String loadGame() {
		String line = "";
		File file = new File(current);
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader(current));
				try {
					while((singleLine = reader.readLine()) != null) {
						line+=singleLine;
					}	
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void loadSave(String save) {
		String[] global = save.split("/");
		for(int i = 0; i < global.length; i++) {
			String[] infos = global[i].split("%");
			switch(infos[0]) {
				case "Details":
					details:
					for(Entity e : Game.entities) {
						if(e instanceof DetailsLabel) {
							for(int ii = 0; ii < ((DetailsLabel) e).labels.size(); ii++) {
								((TextLabel) ((DetailsLabel) e).labels.get(ii)).text = infos[ii+1];
							}
							break details;
						}
					}
					break;
				case "DetailsX":
					for(Entity e : Game.entities) {
						details:
						if(e instanceof DetailsLabel) {
							for(int ii = 0; ii < ((DetailsLabel) e).labels.size(); ii++) {
								((TextLabel) ((DetailsLabel) e).labels.get(ii)).setX(Integer.parseInt(infos[ii+1]));
							}
							break details;
						}
					}
					break;
				case "Stats":
					stats:
					for(Entity e : Game.entities) {
						if(e instanceof StatsLabel) {
							int dices = 0;
							for(int ii = 0; ii < ((StatsLabel) e).labels.size(); ii++) {
								if(((StatsLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((StatsLabel) e).labels.get(ii)).text = infos[ii+1-dices];
								}else if(((StatsLabel) e).labels.get(ii) instanceof CheckBox) {
									String[] check = infos[ii+1-dices].split("<>");
									((CheckBox) ((StatsLabel) e).labels.get(ii)).getArrayList().get(0).text = check[0];
									((CheckBox) ((StatsLabel) e).labels.get(ii)).setChecked(Boolean.parseBoolean(check[1]));
								}else {
									dices++;
								}
							}
							break stats;
						}
					}
					break;
				case "StatsX":
					for(Entity e : Game.entities) {
						stats:
						if(e instanceof StatsLabel) {
							int dices = 0;
							for(int ii = 0; ii < ((StatsLabel) e).labels.size(); ii++) {
								if(((StatsLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((StatsLabel) e).labels.get(ii)).setX(Integer.parseInt(infos[ii+1-dices]));
								}else if(((StatsLabel) e).labels.get(ii) instanceof Dice) {
									dices++;
								}
							}
							break stats;
						}
					}
					break;
				case "Atributes":
					atributes:
						for(Entity e : Game.entities) {
							if(e instanceof AtributosLabel) {
								int dices = 0;
								for(int ii = 0; ii < ((AtributosLabel) e).labels.size(); ii++) {
									if(((AtributosLabel) e).labels.get(ii) instanceof TextLabel) {
										((TextLabel) ((AtributosLabel) e).labels.get(ii)).text = infos[ii+1-dices];
									}else {
										dices++;
									}
								}
								break atributes;
							}
						}
					break;
				case "AtributesX":
					for(Entity e : Game.entities) {
						atributes:
						if(e instanceof AtributosLabel) {
							int dices = 0;
							for(int ii = 0; ii < ((AtributosLabel) e).labels.size(); ii++) {
								if(((AtributosLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((AtributosLabel) e).labels.get(ii)).setX(Integer.parseInt(infos[ii+1-dices]));
								}else if(((AtributosLabel) e).labels.get(ii) instanceof Dice) {
									dices++;
								}
							}
							break atributes;
						}
					}
					break;
				case "Icon":
					for(Entity e : Game.entities) {
						if(e instanceof IconLabel) {
							((CharacterIcon) ((IconLabel) e).labels.get(0)).setIcon2(infos[1]);
						}
					}
					break;
				case "FastSkillsLabel":
					for(Entity e : Game.entities) {
						if(e instanceof FastSkillsLabel) {
							int buttons = 0;
							for(int ii = 0; ii < ((FastSkillsLabel) e).labels.size(); ii++) {
								if(((FastSkillsLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((FastSkillsLabel) e).labels.get(ii)).text = infos[ii+1-buttons];
								}else {
									buttons++;
								}
							}
						}
					}
					break;
				case "FastSkillsLabelX":
					for(Entity e : Game.entities) {
						if(e instanceof FastSkillsLabel) {
							for(int ii = 0; ii < ((FastSkillsLabel) e).labels.size(); ii++) {
								int buttons = 0;
								if(((FastSkillsLabel) e).labels.get(ii) instanceof TextLabel) {
									((FastSkillsLabel) e).labels.get(ii).setX(Integer.parseInt(infos[ii+1-buttons]));
								}else {
									buttons++;
								}
							}
						}
					}
					break;
				case "FastSkills":
					for(Entity e : Game.entities) {
						if(e instanceof FastSkillsLabel) {
							for(int ii = 1; ii < infos.length; ii++) {
								((FastSkillsLabel) e).createLabel(Integer.parseInt(infos[ii]));
							}
						}
					}
					break;
				case "CharacterLabel":
					for(Entity e : Game.entities) {
						if(e instanceof CharacterLabel) {
							for(String s : infos) {
								if(!s.equals("CharacterLabel")) {
									((CharacterLabel) e).addPath(s);
								}
							}
						}
					}
					break;
				case "Guns":
					for(Entity e : Game.entities) {
						if(e instanceof CombatLabel) {
							for(int ii = 0; ii < Integer.parseInt(infos[1]); ii++) {
								((CombatLabel) e).addB.actionPerformed();
								int minus = 0;
								for(int iii = ii*10; iii < 10*(ii+1)+minus; iii++) {
									if(((CombatLabel) e).getGunArrayList().get(ii).labels.get(iii-ii*10) instanceof TextLabel) {
										((TextLabel) ((CombatLabel) e).getGunArrayList().get(ii).labels.get(iii-ii*10)).text = infos[iii+2-minus];
									}else {
										minus++;
									}
								}
							}
						}
					}
					break;
				case "CombatLabel":
					for(Entity e : Game.entities) {
						if(e instanceof CombatLabel) {
							int minus = 0;
							for(int ii = 0; ii < ((CombatLabel) e).labels.size(); ii++) {
								if(((CombatLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((CombatLabel) e).labels.get(ii)).text = infos[ii+1-minus];
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "CombatLabelX":
					for(Entity e : Game.entities) {
						if(e instanceof CombatLabel) {
							int minus = 0;
							for(int ii = 0; ii < ((CombatLabel) e).labels.size(); ii++) {
								if(((CombatLabel) e).labels.get(ii) instanceof TextLabel) {
									((CombatLabel) e).labels.get(ii).setX(Integer.parseInt(infos[ii+1-minus]));
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "RitualsLabel":
					for(Entity e : Game.entities) {
						if(e instanceof Rituals) {
							int minus = 0;
							for(int ii = 0; ii < ((Rituals) e).labels.size(); ii++) {
								if(((Rituals) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((Rituals) e).labels.get(ii)).text = infos[ii+1-minus];
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "RitualsLabelX":
					for(Entity e : Game.entities) {
						if(e instanceof Rituals) {
							int minus = 0;
							for(int ii = 0; ii < ((Rituals) e).labels.size(); ii++) {
								if(((Rituals) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((Rituals) e).labels.get(ii)).setX(Integer.parseInt(infos[ii+1-minus]));
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "Rituals":
					for(Entity e : Game.entities) {					
						if(e instanceof Rituals) {
							for(int ii = 0; ii < Integer.parseInt(infos[1]); ii++) {
								((Rituals) e).addB.actionPerformed();
								int minus = 0;
								for(int iii = ii*11; iii < 11*(ii+1)+minus; iii++) {
									if(!(iii+1 < 11*(ii+1)+minus)) {
										((Rituals) e).getRituals().get(ii).setIcon2(infos[iii+2-minus]);
										continue;
									}
									if(((Rituals) e).getRituals().get(ii).labels.get(iii-ii*11) instanceof TextLabel) {
										((TextLabel) ((Rituals) e).getRituals().get(ii).labels.get(iii-ii*11)).text = infos[iii+2-minus];
									}else {
										minus++;
									}
								}
							}
						}
					}
					break;
				case "Skills":
					for(Entity e : Game.entities) {
						if(e instanceof Skills) {
							int minus = 0;
							for(int ii = 1; ii < ((Skills) e).labels.size(); ii++) {
								if(((Skills) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((Skills) e).labels.get(ii)).text = infos[ii-minus];
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "SkillsRoll":
					for(Entity e : Game.entities) {
						if(e instanceof Skills) {
							for(int ii = 0; ii < ((Skills) e).getSkills().size(); ii++) {
								String[] skill = infos[ii+1].split("<>");
								((TextLabel) ((Skills) e).getSkills().get(ii).labels.get(0)).text = skill[0];
								((TextLabel) ((Skills) e).getSkills().get(ii).labels.get(1)).text = skill[1];
							}
						}
					}
					break;
				case "Inventory":
					for(Entity e : Game.entities) {
						if(e instanceof InventoryLabel) {
							int minus = 0;
							for(int ii = 0; ii < ((InventoryLabel) e).labels.size(); ii++) {
								if(((InventoryLabel) e).labels.get(ii) instanceof TextLabel) {
									((TextLabel) ((InventoryLabel) e).labels.get(ii)).text = infos[ii+1-minus];
								}else {
									minus++;
								}
							}
						}
					}
					break;
				case "Itens":
					for(Entity e : Game.entities) {					
						if(e instanceof InventoryLabel) {
							for(int ii = 1; ii < infos.length; ii++) {
								((InventoryLabel) e).addB.actionPerformed();
								String[] itemInf = infos[ii].split("<>");
								int minus = 0;
								for(int iii = 0; iii < itemInf.length+minus; iii++) {
									if(((InventoryLabel) e).getItemArrayList().get(ii-1).labels.get(iii) instanceof TextLabel) {
										((TextLabel) ((InventoryLabel) e).getItemArrayList().get(ii-1).labels.get(iii)).text = itemInf[iii-minus];
									}else {
										minus++;
									}
								}
							}
						}
					}
					break;
			}
		}
	}

	public static String returnLife(String save) {
		String retorno = "";
		String[] global = save.split("/");
		for(int i = 0; i < global.length; i++) {
			String[] infos = global[i].split("%");
			switch(infos[0]) {
				case "Stats":
					stats:
						for(Entity e : Game.entities) {
							if(e instanceof StatsLabel) {
								int dices = 0;
								for(int ii = 5; ii < 7+dices; ii++) {
									if(((StatsLabel) e).labels.get(ii) instanceof TextLabel) {
										retorno+=infos[ii+1-dices];
										if((ii+1<8)) {
											retorno+="/";
										}
									}else {
										dices++;
									}
								}
								break stats;
							}
						}
					break;
			}
		}
		return retorno;
	}
	
	public static ImageIcon returnIcon(String save) {
		ImageIcon retorno = null;
		String[] global = save.split("/");
		for(int i = 0; i < global.length; i++) {
			String[] infos = global[i].split("%");
			switch(infos[0]) {
			case "Icon":
				for(Entity e : Game.entities) {
					if(e instanceof IconLabel) {
						retorno = new ImageIcon(infos[1]);
					}
				}
				break;
			}
		}
		return retorno;
	}

	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
