package mjaroslav.progs.mcdepbuilder;

import java.util.ArrayList;

import mjaroslav.utils.JavaUtils;

public class DependenciesBuilder {
	public ArrayList<DependenceInfo> list = new ArrayList<DependenceInfo>();

	public static final String DEFAULT_FORMAT = "Mod ID: \"%s\", required: \"%s\", priority: \"%s\", minimal version: \"%s\", maximal version: \"%s\"";

	public String format = DEFAULT_FORMAT;

	public String[] toInfoList() {
		String[] result = new String[list.size()];
		for (int id = 0; id < list.size(); id++) {
			DependenceInfo info = list.get(id);
			result[id] = String.format(format, info.modid, info.isRequired, info.priority.value, info.getMinVersion(),
					info.getMaxVersion());
		}
		return result;
	}

	public String[] toList() {
		ArrayList<String> result = new ArrayList<String>();
		for (DependenceInfo info : list)
			result.add(info.build());
		return result.toArray(new String[] {});
	}

	public void add(String modid, PriorityType priorityType, boolean isRequired, VersionType versionSensitivity,
			String version) {
		add(new DependenceInfo(modid, priorityType, isRequired, versionSensitivity, version));
	}

	public void add(String modid, PriorityType priorityType, boolean isRequired) {
		add(new DependenceInfo(modid, priorityType, isRequired));
	}

	public void add(String modid) {
		add(new DependenceInfo(modid));
	}

	public void add(DependenceInfo dependence) {
		if (dependence != null && JavaUtils.stringIsNotEmpty(dependence.modid))
			if (!contains(dependence))
				list.add(dependence);
	}

	public boolean contains(DependenceInfo dependence) {
		if (dependence != null && JavaUtils.stringIsNotEmpty(dependence.modid))
			for (DependenceInfo check : list)
				if (JavaUtils.stringIsNotEmpty(check.modid))
					if (check.modid.equals(dependence.modid))
						return true;
		return false;
	}

	public void remove(String modid) {
		remove(new DependenceInfo(modid, PriorityType.NONE, false, VersionType.NONE, "*"));
	}

	public void remove(DependenceInfo dependence) {
		if (dependence != null) {
			ArrayList<DependenceInfo> result = new ArrayList<DependenceInfo>();
			for (DependenceInfo check : list)
				if (!check.equals(dependence))
					result.add(check);
			list = result;
		}
	}

	public String build() {
		String result = "";
		for (DependenceInfo dependence : list)
			result += dependence.build();
		return result;
	}

	public static class DependenceInfo {
		public String modid;

		public PriorityType priority;

		public boolean isRequired;

		public VersionType versionSensitivity;

		public String version;

		public DependenceInfo(String modid, PriorityType priorityType, boolean isRequired,
				VersionType versionSensitivity, String version) {
			if (JavaUtils.stringIsNotEmpty(modid)) {
				this.modid = modid;
			} else
				throw new NullPointerException("Mod id not must be null.");
			if (priorityType.equals(PriorityType.NONE))
				throw new NullPointerException("Priority not must be null.");
			else
				this.priority = priorityType;
			this.isRequired = isRequired;
			if (versionSensitivity != null && !versionSensitivity.equals(VersionType.NONE)) {
				this.versionSensitivity = versionSensitivity;
				if (JavaUtils.stringIsNotEmpty(version)) {
					this.version = version;
				} else
					throw new NullPointerException("If you use version, you must select not null value of version.");
			} else
				this.versionSensitivity = VersionType.NONE;
		}

		public DependenceInfo(String modid, PriorityType priorityType, boolean isRequired) {
			this(modid, priorityType, isRequired, VersionType.NONE, "");
		}

		public DependenceInfo(String modid) {
			this(modid, PriorityType.AFTER, true);
		}

		public String getMinVersion() {
			return versionSensitivity == VersionType.NONE ? "*" : version;
		}

		public String getMaxVersion() {
			return versionSensitivity == VersionType.NONE ? "*"
					: versionSensitivity == VersionType.SINGLE ? version : "*";
		}

		public String build() {
			return priority.build(isRequired) + modid + versionSensitivity.build(version) + ";";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			else if (obj instanceof DependenceInfo) {
				DependenceInfo dep = (DependenceInfo) obj;
				return modid.equals(dep.modid) && (!isRequired || !dep.isRequired || isRequired == dep.isRequired)
						&& (priority == PriorityType.NONE || dep.priority == PriorityType.NONE
								|| priority == dep.priority)
						&& ((versionSensitivity == VersionType.NONE || dep.versionSensitivity == VersionType.NONE)
								? true
								: (versionSensitivity.equals(dep.versionSensitivity) && (version.equals("*")
										|| dep.version.equals("*") || version.equals(dep.version))));
			}
			return false;
		}
	}

	public static enum PriorityType {
		AFTER("after"), BEFORE("before"), NONE("");

		private String value;

		private PriorityType(String value) {
			this.value = value;
		}

		public String build(boolean isRequired) {
			if (isRequired) {
				if (JavaUtils.stringIsNotEmpty(value))
					return "required-" + value + ":";
				else
					throw new IllegalArgumentException("Priority type is null");
			} else if (JavaUtils.stringIsNotEmpty(value))
				return value + ":";
			else
				throw new IllegalArgumentException("Priority type is null");
		}
	}

	public static enum VersionType {
		MIN(",)"), SINGLE("]"), NONE("");

		private String suffix;

		private VersionType(String suffix) {
			this.suffix = suffix;
		}

		public String build(String value) {
			if (JavaUtils.stringIsNotEmpty(value) && JavaUtils.stringIsNotEmpty(suffix)) {
				return "@[" + value + suffix;
			} else
				return "";
		}
	}
}
