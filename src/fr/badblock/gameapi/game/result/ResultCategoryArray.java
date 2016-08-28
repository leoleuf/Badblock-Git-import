package fr.badblock.gameapi.game.result;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Repr�sente une cat�gorie ayant une construction � double entr�e
 * 
 * @author LeLanN
 */
@Getter
@RequiredArgsConstructor
public class ResultCategoryArray implements ResultCategory {
	private final String categoryName;
	private final String[] fields;

	private final CategoryType type = CategoryType.ARRAY_DATA;

	private final List<ResultCategoryEntry> lines = new ArrayList<>();

	/**
	 * R�cup�re une ligne afin de la modifier
	 * 
	 * @param at
	 *            L'ID
	 * @return L'ID
	 */
	public ResultCategoryEntry getLine(int at) {
		if (at < lines.size())
			return lines.get(at);
		else
			return null;
	}

	/**
	 * Ajoute une ligne � la cat�gorie
	 * 
	 * @param description
	 *            La description de la ligne
	 * @param content
	 *            Le contenu de la ligne
	 * @return L'ID de la nouvelle ligne
	 */
	public int addLine(@NonNull String description, @NonNull String... content) {
		if (content.length != fields.length) {
			throw new IllegalArgumentException("Content must be of the same size than field (" + fields.length + ")");
		}

		lines.add(new ResultCategoryEntry(description, content));
		return lines.size() - 1;
	}

	/**
	 * Enl�ve une ligne � la cat�gorie
	 * 
	 * @param id
	 *            La ligne
	 */
	public void removeLine(int id) {
		if (id < lines.size()) {
			lines.remove(id);
		}
	}

	/**
	 * Repr�sente une ligne pour {@link ResultCategoryLined}
	 * 
	 * @author LeLanN
	 */
	@Getter
	@AllArgsConstructor
	public static class ResultCategoryEntry {
		@Setter
		private String description;
		private String[] contents;
	}
}
