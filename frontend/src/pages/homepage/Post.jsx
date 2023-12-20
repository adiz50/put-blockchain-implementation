import {
    Card,
    Space,
    Tag,
    Typography,
  } from "antd";
import dayjs from "dayjs";

const Post = ({ post }) => {
return (
    <Card
    size="small"
    title={post.title}
    subtitle={dayjs(post.date).format("YYYY-MM-DD HH:mm:ss")}
    >
    <Space align="start" size={32} stlye={{ width: "100%" }}>
        <Space direction="vertical">
            <Space align="center">
                <Tag color="blue">{post.username}</Tag>
            </Space>
            <Space align="center">
                <Typography.Text>
                    <Tag>post.content</Tag>
                </Typography.Text>
            </Space>
        </Space>
    </Space>
    </Card>
);
};

export default Post;
  