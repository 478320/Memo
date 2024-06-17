# 定义变量
PROTOC = protoc
PROTO_SRC_DIR = src/main/proto
PROTO_DEST_DIR = src/main/proto/com

# 所有的 .proto 文件
PROTO_FILES = $(wildcard $(PROTO_SRC_DIR)/*.proto)

# 生成的文件
GENERATED_FILES = $(patsubst $(PROTO_SRC_DIR)/%.proto, $(PROTO_DEST_DIR)/%.java, $(PROTO_FILES))

# 默认目标
all: $(GENERATED_FILES)

# 编译 .proto 文件到 .java
$(PROTO_DEST_DIR)/%.java: $(PROTO_SRC_DIR)/%.proto
	@if not exist "$(PROTO_DEST_DIR)" mkdir "$(PROTO_DEST_DIR)"
	$(PROTOC) --java_out=$(PROTO_DEST_DIR) $<

# 清理生成的文件
clean:
	@if exist "$(PROTO_DEST_DIR)\*" del /Q "$(PROTO_DEST_DIR)\*"
	@if exist "$(PROTO_DEST_DIR)" rmdir /S /Q "$(PROTO_DEST_DIR)"

.PHONY: all clean