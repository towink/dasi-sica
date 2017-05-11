package com.util.knowledge;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.util.data.IterableSet;

import ec.util.MersenneTwisterFast;
import sim.util.Int2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ArrayKnowledgeMap extends GenericKnowledgeMap {
	
	private Knowledge[][] knowledgeMap;
	private int width;
	private int height;
	private EnumMap<Knowledge, Integer> knowledgeCount;

	
	/**
	 * Creates a knowledge map using an array. This is extremely fast for position
	 * lookups and writes, but very slow for other purposes
	 * @param width
	 * @param height
	 */
	public ArrayKnowledgeMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.init();
	}
	
	@Override
	protected void init() {
		super.init();
		this.knowledgeCount = new EnumMap<Knowledge, Integer>(Knowledge.class);
		this.knowledgeMap = new Knowledge[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				this.knowledgeMap[i][j] = Knowledge.UNKNOWN;
	}

	@Override
	public boolean doAddKnowledge(Int2D where, Knowledge knowledge) {
		Knowledge current = this.getKnowledgeAt(where);
		if (current != Knowledge.UNKNOWN) {
			if (current != knowledge) {
				throw new IllegalStateException("Cannot store different knowledges about the same position. Current: " + current + " New:" + knowledge);
			} else {
				return false;
			}
		}
		this.knowledgeMap[where.x][where.y] = knowledge;
		this.knowledgeCount.put(knowledge, this.knowledgeCount.getOrDefault(knowledge, 0) + 1);
		return true;
	}


	@Override
	public void removeKnowledge(Knowledge knowledge) {
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				if (this.knowledgeMap[i][j] == knowledge)
					this.knowledgeMap[i][j] = Knowledge.UNKNOWN;
		this.knowledgeCount.put(knowledge, 0);
	}

	@Override
	public Knowledge removeKnowledge(Int2D where) {
		Knowledge last = this.knowledgeMap[where.x][where.y];
		if (last != Knowledge.UNKNOWN) {
			this.knowledgeCount.put(last, this.knowledgeCount.getOrDefault(last, 0) - 1);
		}
		this.knowledgeMap[where.x][where.y] = Knowledge.UNKNOWN;
		return last;
	}

	@Override
	public void removeAllKnowledge() {
		this.init();
	}

	@Override
	public Knowledge getKnowledgeAt(Int2D where) {
		return this.knowledgeMap[where.x][where.y];
	}

	@Override
	public Knowledge getKnowledgeAt(int x, int y) {
		return this.knowledgeMap[x][y];
	}

	@Override
	public IterableSet<Int2D> getKnowledgeOf(Knowledge knowledge) {
		return new IterableSet<Int2D>() {

			@Override
			public Iterator<Int2D> iterator() {
				return new Iterator<Int2D>() {
					int i = 0;
					int j = -1;
					boolean empty = false;
					
					{
						if (size() == 0)
							this.empty = true;
						else
							this.findNext();
					}
					
					/**
					 * moves the pointer to the next position in the array,
					 * returning false when the position pointed is not
					 * valid, in which case it will also set empty to true
					 * @return
					 */
					private boolean nextArrayPos() {
						j++;
						if (j >= height) {
							j = 0;
							i++;
						}
						if (i >= width) {
							empty = true;
							return false;
						}
						return true;
					}
					
					/**
					 * Leaves the internal pointer at the next ocurrence
					 * of the desired knowledge. If there is no more ocurrences
					 * empty is set to true
					 */
					private void findNext() {
						while (nextArrayPos()) {
							if (knowledgeMap[i][j] == knowledge) {
								return;
							}
						}
					}

					@Override
					public boolean hasNext() {
						return (!empty);
					}

					@Override
					public Int2D next() {
						if(this.hasNext()) {
							Int2D res = new Int2D(i, j);
			                this.findNext(); //leave pointer for the next
			                return res;
			            }
			            throw new NoSuchElementException();
					}
				};
			}

			@Override
			public int size() {
				return knowledgeCount.getOrDefault(knowledge, 0);
			}
			
		};
	}

	@Override
	public IterableSet<Entry<Int2D, Knowledge>> getAllKnowledge() {
		return new IterableSet<Entry<Int2D, Knowledge>>() {
			int size = 0;
			
			{
				for (Entry<Knowledge, Integer> e : knowledgeCount.entrySet())
					size += e.getValue();
			}

			@Override
			public Iterator<Entry<Int2D, Knowledge>> iterator() {
				return new Iterator<Entry<Int2D, Knowledge>>() {
					int i = 0;
					int j = -1;
					boolean empty = false;
					
					{
						this.findNext();
					}
					
					/**
					 * moves the pointer to the next position in the array,
					 * returning false when the position pointed is not
					 * valid, in which case it will also set empty to true
					 * @return
					 */
					private boolean nextArrayPos() {
						j++;
						if (j >= height) {
							j = 0;
							i++;
						}
						if (i >= width) {
							empty = true;
							return false;
						}
						return true;
					}
					
					/**
					 * Leaves the internal pointer at the next ocurrence
					 * of any knowledge != UNKNOWN. If there is no more ocurrences
					 * empty is set to true
					 */
					private void findNext() {
						while (nextArrayPos()) {
							if (knowledgeMap[i][j] != Knowledge.UNKNOWN) {
								return;
							}
						}
					}

					@Override
					public boolean hasNext() {
						return (!empty);
					}

					@Override
					public Entry<Int2D, Knowledge> next() {
						if(this.hasNext()) {
							Int2D res = new Int2D(i, j);
			                this.findNext(); //leave pointer for the next
			                return new Map.Entry<Int2D, Knowledge>() {
			                	Int2D pos = res;
			                	Knowledge k = knowledgeMap[pos.x][pos.y];
			                	
								@Override
								public Int2D getKey() {
									return pos;
								}

								@Override
								public Knowledge getValue() {
									return k;
								}

								@Override
								public Knowledge setValue(Knowledge value) {
									throw new NotImplementedException();
								}
			                	
			                };
			            }
			            throw new NoSuchElementException();
					}
				};
			}

			@Override
			public int size() {
				return this.size;
			}
			
		};
	}

	@Override
	public Int2D getRandomPositionOfKnowledge(Knowledge knowledge, MersenneTwisterFast random) {
		// TODO Auto-generated method stub
		return null;
	}

}
