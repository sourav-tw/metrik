import React, { FC } from "react";
import { LegendRect } from "./LegendRect";
import { Typography } from "antd";
import { GRAY_1 } from "../constants/styles";
import { Row, Col } from "antd";

const { Text, Title } = Typography;

interface ColourLegendProps {
	elite: string;
	high: string;
	medium: string;
	low: string;
}

const textStyle = { color: GRAY_1, fontSize: 12 };

export const ColourLegend: FC<ColourLegendProps> = ({ elite, high, medium, low }) => (
	<div>
		<Title style={{ color: GRAY_1, fontSize: 14 }} level={5}>
			How to evaluate it?
		</Title>
		<Row align={"middle"} justify={"space-between"} css={{ marginBottom: 8 }}>
			<Col>
				<LegendRect color={"green"} text={"ELITE"} />
			</Col>
			<Col>
				<Text style={textStyle}>{elite}</Text>
			</Col>
		</Row>
		<Row align={"middle"} justify={"space-between"} css={{ marginBottom: 8 }}>
			<Col>
				<LegendRect color={"blue"} text={"HIGH"} />
			</Col>
			<Col>
				<Text style={textStyle}>{high}</Text>
			</Col>
		</Row>
		<Row align={"middle"} justify={"space-between"} css={{ marginBottom: 8 }}>
			<Col>
				<LegendRect color={"orange"} text={"MEDIUM"} />
			</Col>
			<Col>
				<Text style={textStyle}>{medium}</Text>
			</Col>
		</Row>
		<Row align={"middle"} justify={"space-between"}>
			<Col>
				<LegendRect color={"red"} text={"LOW"} />
			</Col>
			<Col>
				<Text style={textStyle}>{low}</Text>
			</Col>
		</Row>
	</div>
);
